package co.com.pragma.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class StateResponseDto {
    private Integer id;
    private String name;
    private String description;
}
