package co.com.pragma.model.loanpetition;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LoanPetitionTest {

    @Test
    void builderAndGettersTest() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        LoanPetition petition = LoanPetition.builder()
                .id(id)
                .documentId("123456789")
                .stateId(1)
                .loanTypeId(2)
                .amount(BigDecimal.valueOf(1500.50))
                .termMonths(12)
                .observations("Test Observations")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(id, petition.getId());
        assertEquals("123456789", petition.getDocumentId());
        assertEquals(1, petition.getStateId());
        assertEquals(2, petition.getLoanTypeId());
        assertEquals(BigDecimal.valueOf(1500.50), petition.getAmount());
        assertEquals(12, petition.getTermMonths());
        assertEquals("Test Observations", petition.getObservations());
        assertEquals(now, petition.getCreatedAt());
        assertEquals(now, petition.getUpdatedAt());
    }

    @Test
    void toBuilderTest() {
        LoanPetition original = LoanPetition.builder()
                .id(UUID.randomUUID())
                .documentId("987654321")
                .stateId(2)
                .loanTypeId(3)
                .amount(BigDecimal.valueOf(2000))
                .termMonths(24)
                .observations("Original")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        LoanPetition copy = original.toBuilder()
                .amount(BigDecimal.valueOf(2500))
                .observations("Updated")
                .build();

        assertEquals(original.getId(), copy.getId());
        assertEquals(original.getDocumentId(), copy.getDocumentId());
        assertEquals(BigDecimal.valueOf(2500), copy.getAmount());
        assertEquals("Updated", copy.getObservations());
        assertEquals(original.getTermMonths(), copy.getTermMonths());
    }
}
