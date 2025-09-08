package co.com.pragma.usecase.loanpetition;

import co.com.pragma.model.loanpetition.LoanPetition;
import co.com.pragma.model.loanpetition.LoanReview;
import co.com.pragma.model.loanpetition.gateways.LoanPetitionRepository;
import co.com.pragma.model.loantype.LoanType;
import co.com.pragma.model.loantype.gateways.LoanTypeRepository;
import co.com.pragma.model.state.State;
import co.com.pragma.model.state.gateways.StateRepository;
import co.com.pragma.model.userinfo.UserInfo;
import co.com.pragma.model.userinfo.gateways.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class ListLoanReviewsUseCase {

    private final LoanPetitionRepository loanPetitionRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final StateRepository stateRepository;
    private final UserInfoRepository userInfoRepository;

    public Flux<LoanReview> executeByStateNames(List<String> stateNames, String search, int page, int size) {
        // Nombres por defecto si no envían ninguno
        List<String> namesToSearch = (stateNames == null || stateNames.isEmpty()) ?
                List.of("Pendiente de revisión", "Solicitud rechazada", "Revisión manual") :
                stateNames;

        return stateRepository.findByNames(namesToSearch)  // Flux<State>
                .collectList()                             // Mono<List<State>>
                .flatMapMany(statesList -> {
                    List<Integer> ids = statesList.stream()
                            .map(State::getId)
                            .toList();
                    return loanPetitionRepository.findByStateIds(ids);  // Flux<LoanPetition>
                })
                .flatMap(this::mapToLoanReview)               // Flux<LoanReview>
                .filter(lr -> search == null || lr.getLoanType().toLowerCase().contains(search.toLowerCase()))
                .skip((long) page * size)
                .take(size);
    }

    private Flux<LoanReview> mapToLoanReview(LoanPetition petition) {
        // Traer datos del usuario
        Mono<UserInfo> userMono = userInfoRepository.findByDocumentId(petition.getDocumentId())
                .single(); // asumimos que hay un solo usuario por documentId

        // Obtener el state "Solicitud aprobada"
        Mono<State> approvedStateMono = stateRepository.findByName("Solicitud aprobada");

        // Traer todas las solicitudes aprobadas del usuario y calcular la deuda total
        Mono<BigDecimal> totalApprovedDebtMono = approvedStateMono.flatMapMany(state ->
                        loanPetitionRepository.findPetitionsByDocumentId(petition.getDocumentId())
                                .filter(p -> p.getStateId().equals(state.getId())) // filtramos por stateId
                )
                .map(LoanPetition::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Traer LoanType y State de la solicitud actual
        Mono<LoanType> loanTypeMono = loanTypeRepository.findById(petition.getLoanTypeId());
        Mono<State> stateMono = stateRepository.findById(petition.getStateId());

        // Combinar todos los Monos
        return Mono.zip(userMono, loanTypeMono, stateMono, totalApprovedDebtMono)
                .map(tuple -> {
                    var user = tuple.getT1();
                    var loanType = tuple.getT2();
                    var state = tuple.getT3();
                    var totalApprovedDebt = tuple.getT4();

                    return LoanReview.builder()
                            .nombre(user.getFirstName() + " " + user.getLastName())
                            .email(user.getEmail())
                            .amount(petition.getAmount())
                            .termMonths(petition.getTermMonths())
                            .loanType(loanType.getName())
                            .interestRate(loanType.getInterestRate())
                            .loanState(state.getName())
                            .salarioBase(user.getBaseSalary())
                            .deudaTotalMensualSolicitudesAprobadas(totalApprovedDebt)
                            .build();
                }).flux();
    }

}
