package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.LoanTypeEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface LoanTypeReactiveRepository extends ReactiveCrudRepository<LoanTypeEntity, Integer>,
        ReactiveQueryByExampleExecutor<LoanTypeEntity> {

    Mono<LoanTypeEntity> findByName(String name);
}
