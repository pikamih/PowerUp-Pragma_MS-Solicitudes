package co.com.pragma.usecase.state;

import co.com.pragma.model.state.State;
import co.com.pragma.model.state.gateways.StateRepository;
import co.com.pragma.usecase.common.messages.BusinessException;
import co.com.pragma.usecase.common.messages.MessageCode;
import co.com.pragma.usecase.mock.StateMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StateUseCaseTest {

    private StateRepository stateRepository;
    private StateUseCase useCase;

    @BeforeEach
    void setUp() {
        stateRepository = mock(StateRepository.class);
        useCase = new StateUseCase(stateRepository);
    }

    @Test
    void createState_success() {
        State state = StateMock.sample();

        when(stateRepository.findByName(state.getName())).thenReturn(Mono.empty());
        when(stateRepository.save(any())).thenReturn(Mono.just(state));

        StepVerifier.create(useCase.createState(state))
                .expectNext(state)
                .verifyComplete();
    }

    @Test
    void createState_nameRequired() {
        State state = StateMock.sample().toBuilder().name(null).build();

        StepVerifier.create(useCase.createState(state))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getMessage().contains(MessageCode.STATE_NAME_REQUIRED.toString()))
                .verify();
    }

    @Test
    void getStateById_success() {
        State state = StateMock.sample();

        when(stateRepository.findById(state.getId())).thenReturn(Mono.just(state));

        StepVerifier.create(useCase.getStateById(state.getId()))
                .expectNext(state)
                .verifyComplete();
    }

    @Test
    void getStateById_notFound() {
        Integer id = 99;
        when(stateRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.getStateById(id))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getMessage().contains(MessageCode.STATE_NOT_FOUND_BY_ID.toString()))
                .verify();
    }

    @Test
    void listStates_success() {
        State s1 = StateMock.sample();
        State s2 = StateMock.sample().toBuilder().id(2).name("Rechazada").description("Solicitud rechazada").build();

        when(stateRepository.findAll()).thenReturn(Flux.just(s1, s2));

        StepVerifier.create(useCase.listStates())
                .expectNext(s1)
                .expectNext(s2)
                .verifyComplete();
    }

    @Test
    void updateState_success() {
        State existing = StateMock.sample();
        State updated = StateMock.sampleUpdated();

        when(stateRepository.findById(existing.getId())).thenReturn(Mono.just(existing));
        when(stateRepository.findByName(updated.getName())).thenReturn(Mono.empty()); // simulamos que no hay duplicado
        when(stateRepository.save(any(State.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(useCase.updateState(existing.getId(), updated))
                .expectNextMatches(state ->
                        state.getId().equals(updated.getId()) &&
                                state.getName().equals(updated.getName()) &&
                                state.getDescription().equals(updated.getDescription())
                )
                .verifyComplete();
    }




    @Test
    void deleteState_success() {
        State existing = StateMock.sample();

        when(stateRepository.findById(existing.getId())).thenReturn(Mono.just(existing));
        when(stateRepository.deleteById(existing.getId())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.deleteState(existing.getId()))
                .verifyComplete();
    }
}
