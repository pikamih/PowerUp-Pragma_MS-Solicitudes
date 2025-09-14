package co.com.pragma.api.handler;

import co.com.pragma.api.dto.request.LoanDecisionRequest;
import co.com.pragma.api.dto.request.LoanPetitionRequestDto;
import co.com.pragma.api.dto.response.LoanPetitionResponseDto;
import co.com.pragma.api.mapper.LoanPetitionWebMapper;
import co.com.pragma.jwt.JwtValidator;
import co.com.pragma.model.loanpetition.LoanDecision;
import co.com.pragma.usecase.common.messages.BusinessException;
import co.com.pragma.usecase.common.messages.MessageCode;
import co.com.pragma.usecase.loanpetition.LoanPetitionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class LoanPetitionHandler {

    private final LoanPetitionUseCase loanPetitionUseCase;
    private final LoanPetitionWebMapper mapper;
    private final JwtValidator jwtValidator;

    // Crear una solicitud de préstamo
    public Mono<ServerResponse> createLoanPetition(ServerRequest request) {

        String authHeader = request.headers().firstHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.error(new BusinessException(MessageCode.USER_NOT_AUTHORIZED, new Object[]{authHeader}));
        }
        String token = authHeader.substring(7);

        return jwtValidator.validateToken(token)
                .flatMap(claims -> {
                    String tokenDocumentId = claims.get("documentId", String.class);
                    String tokenRole = claims.get("role", String.class);

                    return request.bodyToMono(LoanPetitionRequestDto.class)
                            .switchIfEmpty(Mono.error(new BusinessException(MessageCode.REQUEST_BODY_EMPTY, new Object[]{})))
                            .flatMap(dto -> loanPetitionUseCase.createLoanPetition(
                                    mapper.toDomain(dto),
                                    tokenDocumentId,
                                    tokenRole
                            ))
                            .map(mapper::toResponse)
                            .flatMap(response -> ServerResponse
                                    .created(URI.create("/api/v1/loan-petitions/" + response.getId()))
                                    .contentType(APPLICATION_JSON)
                                    .bodyValue(response));
                });
    }

    // Obtener solicitud por id
    public Mono<ServerResponse> findByIdLoanPetition(ServerRequest request) {
        return loanPetitionUseCase.findByIdLoanPetition(UUID.fromString(request.pathVariable("id")))
                .map(mapper::toResponse)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(response));
    }

    // Listar todas las solicitudes
    public Mono<ServerResponse> listAllLoanPetition(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(loanPetitionUseCase.listAllLoanPetition()
                        .map(mapper::toResponse), LoanPetitionResponseDto.class);
    }

    // Actualizar solicitud
    public Mono<ServerResponse> updateLoanPetition(ServerRequest request) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return request.bodyToMono(LoanPetitionRequestDto.class)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.REQUEST_BODY_EMPTY, new Object[]{})))
                .flatMap(dto -> loanPetitionUseCase.updateLoanPetition(id, mapper.toDomain(dto)))
                .map(mapper::toResponse)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(response));
    }

    // Eliminar solicitud
    public Mono<ServerResponse> deleteLoanPetition(ServerRequest request) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return loanPetitionUseCase.deleteLoanPetition(id)
                .then(ServerResponse.noContent().build());
    }
}
