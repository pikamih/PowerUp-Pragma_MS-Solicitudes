package co.com.pragma.model.loanpetition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanReview {
    private String nombre;
    private String email;
    private BigDecimal amount;
    private Integer termMonths;
    private String loanType;
    private BigDecimal interestRate;
    private String loanState;
    private BigDecimal salarioBase;
    private BigDecimal deudaTotalMensualSolicitudesAprobadas;
}
