package co.com.pragma.api.handler;

import co.com.pragma.api.dto.request.LoanTypeRequestDto;
import co.com.pragma.api.mapper.LoanTypeWebMapper;
import co.com.pragma.usecase.common.messages.BusinessException;
import co.com.pragma.usecase.common.messages.MessageCode;
import co.com.pragma.usecase.loantype.LoanTypeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class LoanTypeHandler {

    private final LoanTypeUseCase loanTypeUseCase;
    private final LoanTypeWebMapper loanTypeWebMapper;

    public Mono<ServerResponse> createLoanType(ServerRequest request) {
        return request.bodyToMono(LoanTypeRequestDto.class)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.REQUEST_BODY_EMPTY, new Object[]{})))
                .flatMap(dto -> {
                    var loanTypeDomain = loanTypeWebMapper.toDomain(dto);
                    return loanTypeUseCase.createLoanType(loanTypeDomain);
                })
                .flatMap(savedLoanType -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(loanTypeWebMapper.toResponse(savedLoanType)));
    }

    public Mono<ServerResponse> getLoanTypeById(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return loanTypeUseCase.getLoanTypeById(id)
                .flatMap(loanType -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(loanTypeWebMapper.toResponse(loanType)));
    }

    public Mono<ServerResponse> listLoanTypes(ServerRequest request) {
        return loanTypeUseCase.listLoanTypes()
                .collectList()
                .flatMap(loanTypes -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(
                                loanTypes.stream()
                                        .map(loanTypeWebMapper::toResponse)
                                        .toList()
                        )
                );
    }

    public Mono<ServerResponse> updateLoanType(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return request.bodyToMono(LoanTypeRequestDto.class)
                .switchIfEmpty(Mono.error(new BusinessException(MessageCode.REQUEST_BODY_EMPTY, new Object[]{})))
                .flatMap(dto -> {
                    var loanTypeDomain = loanTypeWebMapper.toDomain(dto);
                    return loanTypeUseCase.updateLoanType(id, loanTypeDomain);
                })
                .flatMap(updateLoanType -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(loanTypeWebMapper.toResponse(updateLoanType)));
    }

    public Mono<ServerResponse> deleteLoanType(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return loanTypeUseCase.deleteLoanType(id)
                .then(ServerResponse.noContent().build());
    }
}
