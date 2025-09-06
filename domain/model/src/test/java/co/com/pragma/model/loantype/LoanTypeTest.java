package co.com.pragma.model.loantype;

import co.com.pragma.model.mock.LoanTypeMock;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LoanTypeTest {

    @Test
    void builderAndGettersTest() {
        LoanType loanType = LoanType.builder()
                .id(1)
                .name("Personal")
                .minAmount(BigDecimal.valueOf(1000))
                .maxAmount(BigDecimal.valueOf(5000))
                .interestRate(BigDecimal.valueOf(5.5))
                .automaticValidation(true)
                .build();

        assertEquals(1, loanType.getId());
        assertEquals("Personal", loanType.getName());
        assertEquals(BigDecimal.valueOf(1000), loanType.getMinAmount());
        assertEquals(BigDecimal.valueOf(5000), loanType.getMaxAmount());
        assertEquals(BigDecimal.valueOf(5.5), loanType.getInterestRate());
        assertTrue(loanType.getAutomaticValidation());
    }

    @Test
    void toBuilderTest() {
        LoanType original = LoanType.builder()
                .id(2)
                .name("Business")
                .minAmount(BigDecimal.valueOf(2000))
                .maxAmount(BigDecimal.valueOf(10000))
                .interestRate(BigDecimal.valueOf(8.0))
                .automaticValidation(false)
                .build();

        LoanType copy = original.toBuilder()
                .interestRate(BigDecimal.valueOf(10.0))
                .automaticValidation(true)
                .build();

        assertEquals(original.getId(), copy.getId());
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getMinAmount(), copy.getMinAmount());
        assertEquals(original.getMaxAmount(), copy.getMaxAmount());
        assertEquals(BigDecimal.valueOf(10.0), copy.getInterestRate());
        assertTrue(copy.getAutomaticValidation());
    }

    @Test
    void mockSampleTest() {
        LoanType sample = LoanTypeMock.sample();
        assertEquals(1, sample.getId());
        assertEquals("Personal", sample.getName());
        assertEquals(BigDecimal.valueOf(1000), sample.getMinAmount());
        assertEquals(BigDecimal.valueOf(5000), sample.getMaxAmount());
        assertEquals(BigDecimal.valueOf(5.5), sample.getInterestRate());
        assertTrue(sample.getAutomaticValidation());
    }

    @Test
    void mockSampleUpdatedTest() {
        LoanType sampleUpdated = LoanTypeMock.sampleUpdated();
        assertEquals(1, sampleUpdated.getId());
        assertEquals("Actualizado", sampleUpdated.getName());
        assertEquals(BigDecimal.valueOf(1500), sampleUpdated.getMinAmount());
        assertEquals(BigDecimal.valueOf(6000), sampleUpdated.getMaxAmount());
        assertEquals(BigDecimal.valueOf(6.0), sampleUpdated.getInterestRate());
        assertFalse(sampleUpdated.getAutomaticValidation());
    }
}
