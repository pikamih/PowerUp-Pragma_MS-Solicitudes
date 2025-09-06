package co.com.pragma.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanPetitionResponseDto {

    private UUID id;
    private String documentId;
    private Integer stateId;
    private Integer loanTypeId;
    private BigDecimal amount;
    private Integer termMonths;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String observations;
}
