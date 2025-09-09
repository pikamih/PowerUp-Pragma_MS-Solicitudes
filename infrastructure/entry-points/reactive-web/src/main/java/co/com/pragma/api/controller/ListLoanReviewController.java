package co.com.pragma.api.controller;

import co.com.pragma.api.dto.response.ListLoanReviewResponseDto;
import co.com.pragma.api.mapper.ListLoanReviewWebMapper;
import co.com.pragma.usecase.loanpetition.ListLoanReviewsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loan-reviews")
@RequiredArgsConstructor
@Tag(name = "Loan Reviews", description = "APIs for reviewing loan petitions")
public class ListLoanReviewController {

    private final ListLoanReviewsUseCase listLoanReviewsUseCase;
    private final ListLoanReviewWebMapper loanReviewWebMapper;

    @Operation(
            summary = "List loan petitions that need manual review",
            description = "Allows filtering by state names, searching by loan type, and pagination"
    )
    @GetMapping
    public Flux<ListLoanReviewResponseDto> listLoanReviews(
            @Parameter(description = "Optional list of state names to filter", example = "Pendiente de revisión,Solicitud rechazada,Revisión manual")
            @RequestParam(value = "state", required = false) List<String> stateNames,
            @Parameter(description = "Optional search term for loan type", example = "Personal")
            @RequestParam(value = "search", required = false) String search,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(value = "size", defaultValue = "10") int size) {

        // Definir estados por defecto si no se envían
        List<String> namesToFilter = (stateNames != null && !stateNames.isEmpty())
                ? stateNames
                : List.of("Pendiente de revisión", "Solicitud rechazada", "Revisión manual");

        // Pasar parámetros al UseCase que ahora maneja filtrado y paginación
        return listLoanReviewsUseCase.executeByStateNames(namesToFilter, search, page, size)
                .map(loanReviewWebMapper::toResponseDto);
    }
}
