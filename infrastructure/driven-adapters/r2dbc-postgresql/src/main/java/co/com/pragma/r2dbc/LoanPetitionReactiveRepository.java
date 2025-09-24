package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.LoanPetitionEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.UUID;

public interface LoanPetitionReactiveRepository extends ReactiveCrudRepository<LoanPetitionEntity, UUID>,
        ReactiveQueryByExampleExecutor<LoanPetitionEntity> {

    Flux<LoanPetitionEntity> findByDocumentId(String documentId);

    Flux<LoanPetitionEntity> findByStateIdIn(Collection<Integer> stateIds);

}
