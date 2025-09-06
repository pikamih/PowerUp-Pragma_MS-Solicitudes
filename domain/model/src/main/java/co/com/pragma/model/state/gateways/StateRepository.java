package co.com.pragma.model.state.gateways;

import co.com.pragma.model.state.State;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StateRepository {

    // Crear o actualizar estado
    Mono<State> save(State state);
    // Buscar estado por ID
    Mono<State> findById(Integer id);
    // Buscar estado por nombre (unicidad)
    Mono<State> findByName(String name);
    // Listar todos los estados
    Flux<State> findAll();
    // Eliminar estado por ID
    Mono<Void> deleteById(Integer id);
}
