package co.com.pragma.api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanTypeRequestDto {

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres.")
    private String name;

    @NotNull(message = "El monto mínimo es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = true, message = "El monto mínimo debe ser mayor o igual a 0.")
    private BigDecimal minAmount;

    @NotNull(message = "El monto máximo es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = true, message = "El monto máximo debe ser mayor o igual a 0.")
    private BigDecimal maxAmount;

    @NotNull(message = "La tasa de interés es obligatoria.")
    @DecimalMin(value = "0.0", inclusive = true, message = "La tasa de interés debe ser mayor o igual a 0.")
    private BigDecimal interestRate;

    @Builder.Default
    private Boolean automaticValidation = false;
}
