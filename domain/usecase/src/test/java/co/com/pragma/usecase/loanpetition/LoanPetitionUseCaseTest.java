package co.com.pragma.usecase.loanpetition;

import co.com.pragma.model.loanpetition.LoanPetition;
import co.com.pragma.model.loanpetition.gateways.LoanPetitionRepository;
import co.com.pragma.model.loantype.LoanType;
import co.com.pragma.model.loantype.gateways.LoanTypeRepository;
import co.com.pragma.model.state.State;
import co.com.pragma.model.state.gateways.StateRepository;
import co.com.pragma.usecase.common.messages.BusinessException;
import co.com.pragma.usecase.common.messages.MessageCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoanPetitionUseCaseTest {

    private LoanPetitionRepository loanPetitionRepository;
    private LoanTypeRepository loanTypeRepository;
    private StateRepository stateRepository;
    private LoanPetitionUseCase useCase;

    @BeforeEach
    void setUp() {
        loanPetitionRepository = mock(LoanPetitionRepository.class);
        loanTypeRepository = mock(LoanTypeRepository.class);
        stateRepository = mock(StateRepository.class);

        useCase = new LoanPetitionUseCase(loanPetitionRepository, loanTypeRepository, stateRepository);
    }

    // --- DataMock ---
    private LoanPetition mockLoanPetition() {
        return LoanPetition.builder()
                .id(UUID.randomUUID())
                .documentId("123456789")
                .loanTypeId(1)
                .stateId(1)
                .amount(BigDecimal.valueOf(1000.0))
                .termMonths(12)
                .observations("Test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private LoanType mockLoanType() {
        return LoanType.builder()
                .id(1)
                .name("Personal")
                .build();
    }

    private State mockState() {
        return State.builder()
                .id(1)
                .name("Pendiente de revisión")
                .build();
    }

    // --- TESTS ---

    @Test
    void createLoanPetition_success() {
        LoanPetition loanPetition = mockLoanPetition();

        when(stateRepository.findByName("Pendiente de revisión")).thenReturn(Mono.just(mockState()));
        when(loanTypeRepository.findById(loanPetition.getLoanTypeId())).thenReturn(Mono.just(mockLoanType()));
        when(loanPetitionRepository.save(any())).thenReturn(Mono.just(loanPetition));

        StepVerifier.create(useCase.createLoanPetition(loanPetition))
                .expectNext(loanPetition)
                .verifyComplete();
    }


    @Test
    void createLoanPetition_missingDocumentId() {
        LoanPetition loanPetition = mockLoanPetition().toBuilder().documentId(null).build();

        StepVerifier.create(useCase.createLoanPetition(loanPetition))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        e.getMessage().contains(MessageCode.LOAN_PETITION_DOCUMENT_ID_REQUIRED.toString()))
                .verify();

    }

    @Test
    void createLoanPetition_missingLoanTypeId() {
        LoanPetition loanPetition = mockLoanPetition().toBuilder().loanTypeId(null).build();

        StepVerifier.create(useCase.createLoanPetition(loanPetition))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                         e.getMessage().contains(MessageCode.LOAN_PETITION_TYPE_ID_REQUIRED.toString()))
                .verify();
    }

    @Test
    void findByIdLoanPetition_success() {
        LoanPetition loanPetition = mockLoanPetition();
        when(loanPetitionRepository.findById(loanPetition.getId())).thenReturn(Mono.just(loanPetition));

        StepVerifier.create(useCase.findByIdLoanPetition(loanPetition.getId()))
                .expectNext(loanPetition)
                .verifyComplete();
    }

    @Test
    void findByIdLoanPetition_notFound() {
        UUID id = UUID.randomUUID();
        when(loanPetitionRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.findByIdLoanPetition(id))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        e.getMessage().contains(MessageCode.LOAN_PETITION_NOT_FOUND.toString()))
                .verify();
    }

    @Test
    void listAllLoanPetition_success() {
        LoanPetition loan1 = mockLoanPetition();
        LoanPetition loan2 = mockLoanPetition();
        when(loanPetitionRepository.findAll()).thenReturn(Flux.just(loan1, loan2));

        StepVerifier.create(useCase.listAllLoanPetition())
                .expectNext(loan1)
                .expectNext(loan2)
                .verifyComplete();
    }

    @Test
    void updateLoanPetition_success() {
        LoanPetition existing = mockLoanPetition();
        LoanPetition updated = existing.toBuilder().amount(BigDecimal.valueOf(2000.0)).build();

        when(loanPetitionRepository.findById(existing.getId())).thenReturn(Mono.just(existing));
        when(stateRepository.findById(any())).thenReturn(Mono.just(mockState()));
        when(loanTypeRepository.findById(any())).thenReturn(Mono.just(mockLoanType()));
        when(loanPetitionRepository.save(any())).thenReturn(Mono.just(updated));

        StepVerifier.create(useCase.updateLoanPetition(existing.getId(), updated))
                .expectNext(updated)
                .verifyComplete();
    }

    @Test
    void deleteLoanPetition_success() {
        LoanPetition existing = mockLoanPetition();
        when(loanPetitionRepository.findById(existing.getId())).thenReturn(Mono.just(existing));
        when(loanPetitionRepository.deleteById(existing.getId())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.deleteLoanPetition(existing.getId()))
                .verifyComplete();
    }

    @Test
    void deleteLoanPetition_notFound() {
        UUID id = UUID.randomUUID();
        when(loanPetitionRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.deleteLoanPetition(id))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        e.getMessage().contains(MessageCode.LOAN_PETITION_NOT_FOUND.toString()))
                .verify();
    }
}
