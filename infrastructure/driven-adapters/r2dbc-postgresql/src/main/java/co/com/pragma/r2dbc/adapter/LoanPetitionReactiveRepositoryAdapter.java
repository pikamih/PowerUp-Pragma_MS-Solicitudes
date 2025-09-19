package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.loanpetition.LoanPetition;
import co.com.pragma.model.loanpetition.gateways.LoanPetitionRepository;
import co.com.pragma.r2dbc.LoanPetitionReactiveRepository;
import co.com.pragma.r2dbc.mapper.LoanPetitionEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoanPetitionReactiveRepositoryAdapter implements LoanPetitionRepository {

    private final LoanPetitionReactiveRepository repository;
    private final TransactionalOperator transactionalOperator;
    private final LoanPetitionEntityMapper loanPetitionEntityMapper;

    @Override
    public Mono<LoanPetition> save(LoanPetition loanPetition) {
        return transactionalOperator
                .execute(status -> repository.save(loanPetitionEntityMapper.toEntity(loanPetition)))
                .map(loanPetitionEntityMapper::toDomain)
                .single();
    }

    @Override
    public Mono<LoanPetition> findById(UUID id) {
        return repository.findById(id)
                .map(loanPetitionEntityMapper::toDomain);
    }

    @Override
    public Flux<LoanPetition> findAll() {
        return repository.findAll()
                .map(loanPetitionEntityMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return repository.deleteById(id);
    }

    @Override
    public Flux<LoanPetition> findByStateIds(Collection<Integer> stateIds) {
        return repository.findByStateIdIn(stateIds)
                .map(loanPetitionEntityMapper::toDomain);
    }

    @Override
    public Flux<LoanPetition> findPetitionsByDocumentId(String documentId) {
        // Aquí usamos tu repository reactivo que devuelve LoanPetitionEntity
        return repository
                .findByDocumentId(documentId) // devuelve Flux<LoanPetitionEntity> o Mono<LoanPetitionEntity>
                .map(loanPetitionEntityMapper::toDomain); // mapear a tu modelo de dominio LoanPetition
    }



}
