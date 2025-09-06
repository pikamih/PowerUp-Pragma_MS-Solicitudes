package co.com.pragma.usecase.loantype;

import co.com.pragma.model.loantype.LoanType;
import co.com.pragma.model.loantype.gateways.LoanTypeRepository;
import co.com.pragma.usecase.common.messages.BusinessException;
import co.com.pragma.usecase.common.messages.MessageCode;
import co.com.pragma.usecase.mock.LoanTypeMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoanTypeUseCaseTest {

    private LoanTypeRepository loanTypeRepository;
    private LoanTypeUseCase useCase;

    @BeforeEach
    void setUp() {
        loanTypeRepository = mock(LoanTypeRepository.class);
        useCase = new LoanTypeUseCase(loanTypeRepository);
    }

    @Test
    void createLoanType_success() {
        LoanType loanType = LoanTypeMock.sample();

        when(loanTypeRepository.findByName(loanType.getName())).thenReturn(Mono.empty());
        when(loanTypeRepository.save(any())).thenReturn(Mono.just(loanType));

        StepVerifier.create(useCase.createLoanType(loanType))
                .expectNext(loanType)
                .verifyComplete();
    }

    @Test
    void createLoanType_nameRequired() {
        LoanType loanType = LoanTypeMock.sample().toBuilder().name(null).build();

        StepVerifier.create(useCase.createLoanType(loanType))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getMessage().contains(MessageCode.LOAN_TYPE_NAME_REQUIRED.toString()))
                .verify();
    }

    @Test
    void getLoanTypeById_success() {
        LoanType loanType = LoanTypeMock.sample();

        when(loanTypeRepository.findById(loanType.getId())).thenReturn(Mono.just(loanType));

        StepVerifier.create(useCase.getLoanTypeById(loanType.getId()))
                .expectNext(loanType)
                .verifyComplete();
    }

    @Test
    void getLoanTypeById_notFound() {
        Integer id = 99;
        when(loanTypeRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.getLoanTypeById(id))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getMessage().contains(MessageCode.LOAN_TYPE_NOT_FOUND_BY_ID.toString()))
                .verify();
    }

    @Test
    void listLoanTypes_success() {
        LoanType lt1 = LoanTypeMock.sample();
        LoanType lt2 = LoanTypeMock.sample().toBuilder().id(2).name("Hipotecario").build();

        when(loanTypeRepository.findAll()).thenReturn(Flux.just(lt1, lt2));

        StepVerifier.create(useCase.listLoanTypes())
                .expectNext(lt1)
                .expectNext(lt2)
                .verifyComplete();
    }

    @Test
    void updateLoanType_success() {
        LoanType existing = LoanTypeMock.sample();
        LoanType updated = existing.toBuilder().name("Actualizado").build();

        when(loanTypeRepository.findById(existing.getId())).thenReturn(Mono.just(existing));
        when(loanTypeRepository.findByName(updated.getName())).thenReturn(Mono.empty());
        when(loanTypeRepository.save(any())).thenReturn(Mono.just(updated));

        StepVerifier.create(useCase.updateLoanType(existing.getId(), updated))
                .expectNext(updated)
                .verifyComplete();
    }

    @Test
    void deleteLoanType_success() {
        LoanType existing = LoanTypeMock.sample();

        when(loanTypeRepository.findById(existing.getId())).thenReturn(Mono.just(existing));
        when(loanTypeRepository.deleteById(existing.getId())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.deleteLoanType(existing.getId()))
                .verifyComplete();
    }
}
