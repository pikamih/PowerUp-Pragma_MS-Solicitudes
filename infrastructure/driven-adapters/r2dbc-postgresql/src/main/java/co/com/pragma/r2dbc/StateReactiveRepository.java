package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.StateEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface StateReactiveRepository extends ReactiveCrudRepository<StateEntity, Integer>,
        ReactiveQueryByExampleExecutor<StateEntity> {

    Mono<StateEntity> findByName(String name);
    Flux<StateEntity> findByNameIn(Collection<String> names);


}
