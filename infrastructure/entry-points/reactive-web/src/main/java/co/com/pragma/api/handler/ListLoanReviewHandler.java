package co.com.pragma.api.handler;

import co.com.pragma.api.dto.response.ListLoanReviewResponseDto;
import co.com.pragma.api.mapper.ListLoanReviewWebMapper;
import co.com.pragma.usecase.loanpetition.ListLoanReviewsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class ListLoanReviewHandler {

    private final ListLoanReviewsUseCase listLoanReviewsUseCase;
    private final ListLoanReviewWebMapper mapper;

    public Mono<ServerResponse> listLoanReviews(ServerRequest request) {
        // Parámetros de paginación y búsqueda
        int page = request.queryParam("page").map(Integer::parseInt).orElse(0);
        int size = request.queryParam("size").map(Integer::parseInt).orElse(10);
        String search = request.queryParam("search").orElse(null);

        // Parámetro de estado
        String stateParam = request.queryParam("state").orElse(null);
        List<String> stateNames;
        if (stateParam != null && !stateParam.isBlank()) {
            stateNames = Arrays.stream(stateParam.split(","))
                    .map(String::trim)
                    .toList();
        } else {
            stateNames = List.of("Pendiente de revisión", "Solicitud rechazada", "Revisión manual");
        }

        // Llamada al UseCase, delegando paginación y filtrado
        Flux<ListLoanReviewResponseDto> response = listLoanReviewsUseCase
                .executeByStateNames(stateNames, search, page, size)
                .map(mapper::toResponseDto);

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(response, ListLoanReviewResponseDto.class);
    }
}
