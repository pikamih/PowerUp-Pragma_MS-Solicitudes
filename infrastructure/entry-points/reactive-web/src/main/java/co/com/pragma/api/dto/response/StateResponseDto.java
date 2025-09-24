package co.com.pragma.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StateResponseDto {
    private Integer id;
    private String name;
    private String description;
}
