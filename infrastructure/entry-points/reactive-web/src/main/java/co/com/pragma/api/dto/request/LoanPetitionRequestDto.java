package co.com.pragma.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanPetitionRequestDto {

    @NotBlank(message = "El documentId es obligatorio")
    private String documentId;

    @NotNull(message = "El loanTypeId es obligatorio")
    private Integer loanTypeId;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private BigDecimal amount;

    @NotNull(message = "El término en meses es obligatorio")
    @Positive(message = "El término debe ser mayor a 0")
    private Integer termMonths;

    private String observations;
}
