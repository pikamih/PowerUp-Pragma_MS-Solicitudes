package co.com.pragma.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDecisionRequest {
    private String decision; // "Aprobado" o "Rechazado"
}
