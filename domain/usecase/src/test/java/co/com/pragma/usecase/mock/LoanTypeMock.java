package co.com.pragma.usecase.mock;

import co.com.pragma.model.loantype.LoanType;

import java.math.BigDecimal;

public final class LoanTypeMock {

    private LoanTypeMock() {}

    public static LoanType sample() {
        return LoanType.builder()
                .id(1)
                .name("Personal")
                .minAmount(BigDecimal.valueOf(1000))
                .maxAmount(BigDecimal.valueOf(5000))
                .interestRate(BigDecimal.valueOf(5.5))
                .automaticValidation(true)
                .build();
    }

    public static LoanType sampleUpdated() {
        return LoanType.builder()
                .id(1)
                .name("Actualizado")
                .minAmount(BigDecimal.valueOf(1500))
                .maxAmount(BigDecimal.valueOf(6000))
                .interestRate(BigDecimal.valueOf(6.0))
                .automaticValidation(false)
                .build();
    }
}
