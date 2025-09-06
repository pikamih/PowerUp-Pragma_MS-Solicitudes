package co.com.pragma.model.mock;

import co.com.pragma.model.loanpetition.LoanPetition;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public final class LoanPetitionMock {

    private LoanPetitionMock() {}

    public static LoanPetition sample() {
        return LoanPetition.builder()
                .id(UUID.randomUUID())
                .documentId("123456789")
                .loanTypeId(1)
                .stateId(1)
                .amount(BigDecimal.valueOf(1000.0))
                .termMonths(12)
                .observations("Test LoanPetition")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
