package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.loantype.LoanType;
import co.com.pragma.model.loantype.gateways.LoanTypeRepository;
import co.com.pragma.r2dbc.LoanTypeReactiveRepository;
import co.com.pragma.r2dbc.mapper.LoanTypeEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoanTypeReactiveRepositoryAdapter implements LoanTypeRepository {

    private final LoanTypeReactiveRepository repository;
    private final TransactionalOperator transactionalOperator;
    private final LoanTypeEntityMapper loanTypeEntityMapper;

    @Override
    public Mono<LoanType> save(LoanType loanType) {
        return transactionalOperator
                .execute(status -> repository.save(loanTypeEntityMapper.toEntity(loanType)))
                .map(loanTypeEntityMapper::toDomain)
                .single();
    }

    @Override
    public Mono<LoanType> findById(Integer id) {
        return repository.findById(id)
                .map(loanTypeEntityMapper::toDomain);
    }

    @Override
    public Mono<LoanType> findByName(String name) {
        return repository.findByName(name)
                .map(loanTypeEntityMapper::toDomain);
    }

    @Override
    public Flux<LoanType> findAll() {
        return repository.findAll()
                .map(loanTypeEntityMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return repository.deleteById(id);
    }
}
