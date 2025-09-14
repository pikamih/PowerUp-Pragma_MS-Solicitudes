package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.loanpetition.LoanPetition;
import co.com.pragma.model.loanpetition.LoanReview;
import co.com.pragma.model.loanpetition.gateways.LoanPetitionRepository;
import co.com.pragma.model.loanpetition.gateways.LoanReviewRepository;
import co.com.pragma.model.loantype.LoanType;
import co.com.pragma.model.loantype.gateways.LoanTypeRepository;
import co.com.pragma.model.state.State;
import co.com.pragma.model.state.gateways.StateRepository;
import co.com.pragma.model.userinfo.UserInfo;
import co.com.pragma.model.userinfo.gateways.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class LoanReviewMapperService implements LoanReviewRepository {


    private final UserInfoRepository userInfoRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final StateRepository stateRepository;
    private final LoanPetitionRepository loanPetitionRepository;



    public Flux<LoanReview> mapToLoanReview(LoanPetition petition) {
        // Traer datos del usuario
        Mono<UserInfo> userMono = userInfoRepository.findByDocumentId(petition.getDocumentId())
                .single(); // asumimos que hay un solo usuario por documentId
        // Obtener el state "Solicitud aprobada"
        Mono<State> approvedStateMono = stateRepository.findByName("APROBADO");
        // Traer todas las solicitudes aprobadas del usuario y calcular la deuda total
        // Calcular deuda total mensual de solicitudes aprobadas
        Mono<BigDecimal> totalApprovedDebtMono = approvedStateMono.flatMapMany(state ->
                        loanPetitionRepository.findPetitionsByDocumentId(petition.getDocumentId())
                                .filter(p -> p.getStateId().equals(state.getId()))
                )
                .flatMap(approvedPetition -> loanTypeRepository.findById(approvedPetition.getLoanTypeId())
                        .map(loanType -> calculateMonthlyFee(
                                approvedPetition.getAmount(),
                                loanType.getInterestRate(),
                                approvedPetition.getTermMonths()
                        ))
                )
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

                    BigDecimal montoMensualSolicitud = calculateMonthlyFee(
                            petition.getAmount(),
                            loanType.getInterestRate(),
                            petition.getTermMonths()
                    );

                    return LoanReview.builder()
                            .LoanId(petition.getId())
                            .nombre(user.getFirstName() + " " + user.getLastName())
                            .documentId(user.getDocumentId())
                            .email(user.getEmail())
                            .amount(petition.getAmount())
                            .termMonths(petition.getTermMonths())
                            .loanType(loanType.getName())
                            .interestRate(loanType.getInterestRate())
                            .loanState(state.getName())
                            .salarioBase(user.getBaseSalary())
                            .montoMensualSolicitud(montoMensualSolicitud)
                            .deudaTotalMensualSolicitudesAprobadas(totalApprovedDebt)
                            .build();
                }).flux();
    }

    private BigDecimal calculateMonthlyFee(BigDecimal monto, BigDecimal interesAnual, int termMonths) {
        // Pasar de tasa anual (%) a tasa mensual (decimal)
        BigDecimal tasaMensual = interesAnual.divide(BigDecimal.valueOf(100 * 12), 10, RoundingMode.HALF_UP);

        if (tasaMensual.compareTo(BigDecimal.ZERO) == 0) {
            return monto.divide(BigDecimal.valueOf(termMonths), 2, RoundingMode.HALF_UP);
        }

        // Fórmula de cuota: P * i / (1 - (1+i)^-n)
        BigDecimal numerator = monto.multiply(tasaMensual);
        BigDecimal denominator = BigDecimal.ONE.subtract(
                BigDecimal.ONE.add(tasaMensual).pow(-termMonths, new java.math.MathContext(10))
        );

        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

}
