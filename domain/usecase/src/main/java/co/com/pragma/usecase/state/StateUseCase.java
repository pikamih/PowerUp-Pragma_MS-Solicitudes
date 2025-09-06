package co.com.pragma.usecase.state;

import co.com.pragma.model.state.State;
import co.com.pragma.model.state.gateways.StateRepository;
import co.com.pragma.usecase.common.messages.BusinessException;
import co.com.pragma.usecase.common.messages.MessageCode;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class StateUseCase {

    private final StateRepository stateRepository;

    public Mono<State> createState(State state) {
        if (state.getName() == null || state.getName().isBlank()) {
            return Mono.error(new BusinessException(MessageCode.STATE_NAME_REQUIRED, new Object[]{}));
        }
        return stateRepository.findByName(state.getName())
                .flatMap(existing -> Mono.<State>error(new BusinessException(MessageCode.STATE_ALREADY_EXISTS, new Object[]{})))
                .switchIfEmpty(Mono.defer(() ->
                        stateRepository.save(state)
                ));
    }

    public Mono<State> getStateById(Integer id) {
        return stateRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.STATE_NOT_FOUND_BY_ID, new Object[]{id})));
    }

    public Flux<State> listStates() {
        return stateRepository.findAll();
    }

    public Mono<State> updateState(Integer id, State state) {
        if (state.getName() == null || state.getName().isBlank()) {
            return Mono.error(new BusinessException(MessageCode.STATE_NAME_REQUIRED, new Object[]{}));
        }

        return stateRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.STATE_NOT_FOUND_BY_ID, new Object[]{id})))
                .flatMap(existing ->
                        stateRepository.findByName(state.getName())
                                .flatMap(duplicate -> {
                                    if (!duplicate.getId().equals(id)) {
                                        return Mono.error(new BusinessException(MessageCode.STATE_ALREADY_EXISTS, new Object[]{}));
                                    }
                                    existing.setName(state.getName());
                                    existing.setDescription(state.getDescription());
                                    return stateRepository.save(existing);
                                })
                                .switchIfEmpty(Mono.defer(() -> {
                                    existing.setName(state.getName());
                                    existing.setDescription(state.getDescription());
                                    return stateRepository.save(existing);
                                }))
                );
    }


    public Mono<Void> deleteState(Integer id) {
        return stateRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.STATE_NOT_FOUND_BY_ID, new Object[]{id})))
                .flatMap(existing -> stateRepository.deleteById(id));
    }
}
