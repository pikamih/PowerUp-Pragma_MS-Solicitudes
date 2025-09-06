package co.com.pragma.usecase.loantype;

import co.com.pragma.model.loantype.LoanType;
import co.com.pragma.model.loantype.gateways.LoanTypeRepository;
import co.com.pragma.usecase.common.messages.BusinessException;
import co.com.pragma.usecase.common.messages.MessageCode;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class LoanTypeUseCase {

    private final LoanTypeRepository loanTypeRepository;

    public Mono<LoanType> createLoanType(LoanType loanType) {
        // Validaciones básicas
        if (loanType.getName() == null || loanType.getName().isBlank()) {
            return Mono.error(new BusinessException(MessageCode.LOAN_TYPE_NAME_REQUIRED, new Object[]{}));
        }

        if (loanType.getMinAmount() == null) {
            return Mono.error(new BusinessException(MessageCode.LOAN_TYPE_MIN_AMOUNT_REQUIRED, new Object[]{}));
        }
        if (loanType.getMinAmount().compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new BusinessException(MessageCode.LOAN_TYPE_MIN_AMOUNT_INVALID, new Object[]{}));
        }

        if (loanType.getMaxAmount() == null) {
            return Mono.error(new BusinessException(MessageCode.LOAN_TYPE_MAX_AMOUNT_REQUIRED, new Object[]{}));
        }
        if (loanType.getMaxAmount().compareTo(loanType.getMinAmount()) < 0) {
            return Mono.error(new BusinessException(MessageCode.LOAN_TYPE_MAX_AMOUNT_INVALID, new Object[]{}));
        }

        if (loanType.getInterestRate() == null) {
            return Mono.error(new BusinessException(MessageCode.LOAN_TYPE_INTEREST_RATE_REQUIRED, new Object[]{}));
        }
        if (loanType.getInterestRate().compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new BusinessException(MessageCode.LOAN_TYPE_INTEREST_RATE_INVALID, new Object[]{}));
        }


        return loanTypeRepository.findByName(loanType.getName())
                .flatMap(existing -> Mono.<LoanType>error(new BusinessException(MessageCode.LOAN_TYPE_ALREADY_EXISTS, new Object[]{})))
                .switchIfEmpty(loanTypeRepository.save(loanType));
    }

    public Mono<LoanType> getLoanTypeById(Integer id) {
        return loanTypeRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.LOAN_TYPE_NOT_FOUND_BY_ID, new Object[]{id})));
    }

    public Flux<LoanType> listLoanTypes() {
        return loanTypeRepository.findAll();
    }

    public Mono<LoanType> updateLoanType(Integer id, LoanType loanType) {

        // Validaciones básicas
        if (loanType.getName() == null || loanType.getName().isBlank()) {
            return Mono.error(new BusinessException(MessageCode.LOAN_TYPE_NAME_REQUIRED, new Object[]{}));
        }

        if (loanType.getMinAmount() == null) {
            return Mono.error(new BusinessException(MessageCode.LOAN_TYPE_MIN_AMOUNT_REQUIRED, new Object[]{}));
        }
        if (loanType.getMinAmount().compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new BusinessException(MessageCode.LOAN_TYPE_MIN_AMOUNT_INVALID, new Object[]{}));
        }

        if (loanType.getMaxAmount() == null) {
            return Mono.error(new BusinessException(MessageCode.LOAN_TYPE_MAX_AMOUNT_REQUIRED, new Object[]{}));
        }
        if (loanType.getMaxAmount().compareTo(loanType.getMinAmount()) < 0) {
            return Mono.error(new BusinessException(MessageCode.LOAN_TYPE_MAX_AMOUNT_INVALID, new Object[]{}));
        }

        if (loanType.getInterestRate() == null) {
            return Mono.error(new BusinessException(MessageCode.LOAN_TYPE_INTEREST_RATE_REQUIRED, new Object[]{}));
        }
        if (loanType.getInterestRate().compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new BusinessException(MessageCode.LOAN_TYPE_INTEREST_RATE_INVALID, new Object[]{}));
        }

        return loanTypeRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.LOAN_TYPE_NOT_FOUND_BY_ID, new Object[]{id})))
                .flatMap(existing ->
                        loanTypeRepository.findByName(loanType.getName())
                                .defaultIfEmpty(existing)
                                .flatMap(duplicate -> {
                                    if (!duplicate.getId().equals(id)) {
                                        return Mono.error(new BusinessException(MessageCode.LOAN_TYPE_ALREADY_EXISTS, new Object[]{}));
                                    }
                                    existing.setName(loanType.getName());
                                    existing.setMinAmount(loanType.getMinAmount());
                                    existing.setMaxAmount(loanType.getMaxAmount());
                                    existing.setInterestRate(loanType.getInterestRate());
                                    existing.setAutomaticValidation(loanType.getAutomaticValidation());
                                    return loanTypeRepository.save(existing);
                                })
                );


    }

    public Mono<Void> deleteLoanType(Integer id) {
        return loanTypeRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.LOAN_TYPE_NOT_FOUND_BY_ID, new Object[]{id})))
                .flatMap(existing -> loanTypeRepository.deleteById(id));
    }
}
