package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.state.State;
import co.com.pragma.model.state.gateways.StateRepository;
import co.com.pragma.r2dbc.StateReactiveRepository;
import co.com.pragma.r2dbc.mapper.StateEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class StateReactiveRepositoryAdapter implements StateRepository {

    private final StateReactiveRepository repository;
    private final TransactionalOperator transactionalOperator;
    private final StateEntityMapper stateEntityMapper;

    @Override
    public Mono<State> save(State state) {
        return transactionalOperator
                .execute(status -> repository.save(stateEntityMapper.toEntity(state)))
                .map(stateEntityMapper::toDomain)
                .single();
    }

    @Override
    public Mono<State> findById(Integer id) {
        return repository.findById(id)
                .map(stateEntityMapper::toDomain);
    }

    @Override
    public Mono<State> findByName(String name) {
        return repository.findByName(name)
                .map(stateEntityMapper::toDomain);
    }

    @Override
    public Flux<State> findAll() {
        return repository.findAll()
                .map(stateEntityMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return repository.deleteById(id);
    }
}
