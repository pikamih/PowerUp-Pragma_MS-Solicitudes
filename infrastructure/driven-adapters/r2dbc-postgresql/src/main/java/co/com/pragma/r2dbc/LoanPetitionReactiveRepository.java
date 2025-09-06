package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.LoanPetitionEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LoanPetitionReactiveRepository extends ReactiveCrudRepository<LoanPetitionEntity, UUID>,
        ReactiveQueryByExampleExecutor<LoanPetitionEntity> {

    Mono<LoanPetitionEntity> findByDocumentId(String documentId);

}
