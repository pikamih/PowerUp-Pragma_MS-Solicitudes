package co.com.pragma.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StateRequestDto {

    @NotBlank(message = "El nombre no puede estar vació.")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres.")
    private String name;
    private String description;
}
