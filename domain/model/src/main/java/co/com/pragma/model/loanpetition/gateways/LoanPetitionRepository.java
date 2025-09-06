package co.com.pragma.model.loanpetition.gateways;

import co.com.pragma.model.loanpetition.LoanPetition;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LoanPetitionRepository {

    Mono<LoanPetition> save(LoanPetition loanPetition);
    Mono<LoanPetition> findById(UUID id);
    Flux<LoanPetition> findAll();
    Mono<Void> deleteById(UUID id);
}
