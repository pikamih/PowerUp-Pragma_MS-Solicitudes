package co.com.pragma.model.loanpetition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDecision {
    private String loanId;        // Identificador único de la solicitud
    private String decision;      // "APROBADO" o "RECHAZADO"
}
