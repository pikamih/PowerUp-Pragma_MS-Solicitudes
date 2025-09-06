package co.com.pragma.api.handler;

import co.com.pragma.api.dto.request.StateRequestDto;
import co.com.pragma.api.mapper.StateWebMapper;
import co.com.pragma.usecase.common.messages.BusinessException;
import co.com.pragma.usecase.common.messages.MessageCode;
import co.com.pragma.usecase.state.StateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class StateHandler {

    private final StateUseCase stateUseCase;
    private final StateWebMapper stateWebMapper;

    public Mono<ServerResponse> createState(ServerRequest request) {
        return request.bodyToMono(StateRequestDto.class)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.REQUEST_BODY_EMPTY, new Object[]{})))
                .flatMap(dto -> {
                    var stateDomain = stateWebMapper.toDomain(dto);

                    return stateUseCase.createState(stateDomain);
                })
                .flatMap(savedState -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(stateWebMapper.toResponse(savedState)));
    }

    public Mono<ServerResponse> getStateById(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return stateUseCase.getStateById(id)
                .flatMap(state -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(stateWebMapper.toResponse(state)));
    }

    public Mono<ServerResponse> listState(ServerRequest request) {
        return stateUseCase.listStates()
                .collectList()
                .flatMap(states -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(
                                states.stream()
                                        .map(stateWebMapper::toResponse)
                                        .toList()
                        )
                );
    }

    public Mono<ServerResponse> updateState(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return request.bodyToMono(StateRequestDto.class)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.REQUEST_BODY_EMPTY, new Object[]{})))
                .flatMap(dto -> {
                    var stateDomain = stateWebMapper.toDomain(dto);
                    return stateUseCase.updateState(id, stateDomain);
                })
                .flatMap(updateState -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(stateWebMapper.toResponse(updateState)));
    }

    public Mono<ServerResponse> deleteState(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return stateUseCase.deleteState(id)
                .then(ServerResponse.noContent().build());
    }
}
