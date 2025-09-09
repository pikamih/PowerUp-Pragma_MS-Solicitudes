package co.com.pragma.usecase.loanpetition;

import co.com.pragma.model.loanpetition.LoanPetition;
import co.com.pragma.model.loanpetition.gateways.LoanPetitionRepository;
import co.com.pragma.model.loantype.LoanType;
import co.com.pragma.model.loantype.gateways.LoanTypeRepository;
import co.com.pragma.model.state.State;
import co.com.pragma.model.state.gateways.StateRepository;
import co.com.pragma.usecase.common.messages.BusinessException;
import co.com.pragma.usecase.common.messages.MessageCode;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class LoanPetitionUseCase {

    private final LoanPetitionRepository loanPetitionRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final StateRepository stateRepository;

    public Mono<LoanPetition> createLoanPetition(LoanPetition loanPetition, String tokenDocumentId, String tokenRole) {

        if (!"CLIENTE".equalsIgnoreCase(tokenRole)) {
            return Mono.error(new BusinessException(MessageCode.CLIENT_ROLE_UNAUTHORIZED, new Object[]{tokenRole}));
        }
        if (!loanPetition.getDocumentId().equals(tokenDocumentId)) {
            return Mono.error(new BusinessException(MessageCode.USER_NOT_AUTHORIZED, new Object[]{}));
        }

        if (loanPetition.getDocumentId() == null) {
            return Mono.error(new BusinessException(MessageCode.LOAN_PETITION_DOCUMENT_ID_REQUIRED, new Object[]{}));
        }
        if (loanPetition.getLoanTypeId() == null) {
            return Mono.error(new BusinessException(MessageCode.LOAN_PETITION_TYPE_ID_REQUIRED, new Object[]{}));
        }

        return stateRepository.findByName("Pendiente de revisión")
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.LOAN_PETITION_INITIAL_STATE_NOT_FOUND, new Object[]{})))
                .flatMap(initialState -> {
                    loanPetition.setStateId(initialState.getId());
                    return loanTypeRepository.findById(loanPetition.getLoanTypeId())
                            .switchIfEmpty(Mono.error(new BusinessException(MessageCode.LOAN_PETITION_TYPE_NOT_FOUND, new Object[]{})))
                            .flatMap(validLoanType -> loanPetitionRepository.save(loanPetition));
                });

    }

    public Mono<LoanPetition> findByIdLoanPetition(UUID id) {
        return loanPetitionRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.LOAN_PETITION_NOT_FOUND, new Object[]{})));
    }

    public Flux<LoanPetition> listAllLoanPetition() {
        return loanPetitionRepository.findAll();
    }

    public Mono<LoanPetition> updateLoanPetition(UUID id, LoanPetition loanPetition) {
        return loanPetitionRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.LOAN_PETITION_NOT_FOUND, new Object[]{})))
                .flatMap(existing -> {

                    Mono<Integer> validatedState = loanPetition.getStateId() != null
                            ? stateRepository.findById(loanPetition.getStateId())
                            .switchIfEmpty(Mono.error(new BusinessException(MessageCode.LOAN_PETITION_STATE_NOT_FOUND, new Object[]{})))
                            .map(State::getId)
                            : Mono.just(existing.getStateId());

                    Mono<Integer> validatedLoanType = loanPetition.getLoanTypeId() != null
                            ? loanTypeRepository.findById(loanPetition.getLoanTypeId())
                            .switchIfEmpty(Mono.error(new BusinessException(MessageCode.LOAN_PETITION_STATE_NOT_FOUND, new Object[]{})))
                            .map(LoanType::getId)
                            : Mono.just(existing.getLoanTypeId());

                    return Mono.zip(validatedState, validatedLoanType)
                            .flatMap(tuple -> {
                                LoanPetition updated = existing.toBuilder()
                                        .stateId(tuple.getT1())
                                        .loanTypeId(tuple.getT2())
                                        .amount(loanPetition.getAmount() != null ? loanPetition.getAmount() : existing.getAmount())
                                        .termMonths(loanPetition.getTermMonths() != null ? loanPetition.getTermMonths() : existing.getTermMonths())
                                        .observations(loanPetition.getObservations() != null ? loanPetition.getObservations() : existing.getObservations())
                                        .updatedAt(LocalDateTime.now())
                                        .build();
                                return loanPetitionRepository.save(updated);
                            });
                });
    }


    public Mono<Void> deleteLoanPetition(UUID id) {
        return loanPetitionRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.LOAN_PETITION_NOT_FOUND, new Object[]{})))
                .flatMap(existing -> loanPetitionRepository.deleteById(id));
    }
}
