package co.com.pragma.model.loanpetition.gateways;

import co.com.pragma.model.loanpetition.LoanPetition;
import co.com.pragma.model.userinfo.UserInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

public interface LoanPetitionRepository {

    Mono<LoanPetition> save(LoanPetition loanPetition);
    Mono<LoanPetition> findById(UUID id);
    Flux<LoanPetition> findAll();
    Mono<Void> deleteById(UUID id);
    Flux<LoanPetition> findByStateIds(Collection<Integer> stateIds);
    Flux<UserInfo> findByDocumentId(String documentId);
    Flux<LoanPetition> findPetitionsByDocumentId(String documentId);





}
