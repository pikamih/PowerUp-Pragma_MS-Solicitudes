package co.com.pragma.model.loantype.gateways;

import co.com.pragma.model.loantype.LoanType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {

    // Crear o actualizar estado
    Mono<LoanType> save(LoanType loanType);
    // Buscar estado por ID
    Mono<LoanType> findById(Integer id);
    // Buscar estado por nombre (unicidad)
    Mono<LoanType> findByName(String name);
    // Listar todos los estados
    Flux<LoanType> findAll();
    // Eliminar estado por ID
    Mono<Void> deleteById(Integer id);
}
