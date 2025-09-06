package co.com.pragma.r2dbc.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("loan_petition")
public class LoanPetitionEntity {

    @Id
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
