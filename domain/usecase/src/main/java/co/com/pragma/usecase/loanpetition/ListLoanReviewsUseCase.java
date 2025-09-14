package co.com.pragma.usecase.loanpetition;

import co.com.pragma.model.loanpetition.LoanDecision;
import co.com.pragma.model.loanpetition.LoanPetition;
import co.com.pragma.model.loanpetition.LoanReview;
import co.com.pragma.model.loanpetition.gateways.LoanNotificationGateway;
import co.com.pragma.model.loanpetition.gateways.LoanPetitionRepository;
import co.com.pragma.model.loanpetition.gateways.LoanReviewRepository;
import co.com.pragma.model.state.State;
import co.com.pragma.model.state.gateways.StateRepository;
import co.com.pragma.usecase.common.messages.BusinessException;
import co.com.pragma.usecase.common.messages.MessageCode;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ListLoanReviewsUseCase {

    private final LoanPetitionRepository loanPetitionRepository;
    private final StateRepository stateRepository;
    private final LoanNotificationGateway notificationGateway;
    private final LoanReviewRepository loanReviewRepository;

    public Flux<LoanReview> executeByStateNames(List<String> stateNames, String search, int page, int size) {
        // Nombres por defecto si no envían ninguno
        List<String> namesToSearch = (stateNames == null || stateNames.isEmpty()) ?
                List.of("Pendiente de revisión", "RECHAZADO", "Revisión manual") :
                stateNames;

        return stateRepository.findByNames(namesToSearch)  // Flux<State>
                .collectList()                             // Mono<List<State>>
                .flatMapMany(statesList -> {
                    List<Integer> ids = statesList.stream()
                            .map(State::getId)
                            .toList();
                    return loanPetitionRepository.findByStateIds(ids);  // Flux<LoanPetition>
                })
                .flatMap(loanReviewRepository::mapToLoanReview)               // Flux<LoanReview>
                .filter(lr -> search == null || lr.getLoanType().toLowerCase().contains(search.toLowerCase()))
                .skip((long) page * size)
                .take(size);
    }


    public Mono<LoanPetition> approveOrRejectLoan(LoanDecision decision) {
        UUID loanId = UUID.fromString(decision.getLoanId());
        String decisionName = decision.getDecision();

        return stateRepository.findByName(decisionName)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.LOAN_PETITION_STATE_NOT_FOUND, new Object[]{})))
                .flatMap(state -> loanPetitionRepository.findById(loanId)
                        .switchIfEmpty(Mono.error(new BusinessException(MessageCode.LOAN_PETITION_NOT_FOUND, new Object[]{})))
                        .flatMap(loan -> {
                            loan.setStateId(state.getId());
                            loan.setUpdatedAt(LocalDateTime.now());
                            return loanPetitionRepository.save(loan)
                                    .flatMap(savedLoan -> {
                                        String message = String.format("{\"actionType\":\"%s\",\"loanId\":\"%s\",\"status\":\"%s\"}",
                                                "Notify",
                                                savedLoan.getId(),
                                                decisionName);
                                        return notificationGateway.sendMessageSQS(message)
                                                .thenReturn(savedLoan);
                                    });
                        }));
    }





}
