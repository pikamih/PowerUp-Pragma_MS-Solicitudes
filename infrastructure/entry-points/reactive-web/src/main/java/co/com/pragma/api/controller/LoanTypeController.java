package co.com.pragma.api.controller;

import co.com.pragma.api.dto.request.LoanTypeRequestDto;
import co.com.pragma.api.dto.response.LoanTypeResponseDto;
import co.com.pragma.api.mapper.LoanTypeWebMapper;
import co.com.pragma.usecase.loantype.LoanTypeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/loans/loan-types")
@RequiredArgsConstructor
@Tag(name = "Loan Types", description = "APIs for managing loan types")
public class LoanTypeController {

    private final LoanTypeUseCase loanTypeUseCase;
    private final LoanTypeWebMapper loanTypeWebMapper;

    @Operation(summary = "Create a new loan type",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Loan type created",
                            content = @Content(schema = @Schema(implementation = LoanTypeResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<LoanTypeResponseDto> createLoanType(@Valid @RequestBody LoanTypeRequestDto dto) {
        return loanTypeUseCase.createLoanType(loanTypeWebMapper.toDomain(dto))
                .map(loanTypeWebMapper::toResponse);
    }

    @Operation(summary = "Get all loan types",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of loan types",
                            content = @Content(schema = @Schema(implementation = LoanTypeResponseDto.class)))
            })
    @GetMapping
    public Mono<ResponseEntity<Flux<LoanTypeResponseDto>>> getAllLoanTypes() {
        Flux<LoanTypeResponseDto> flux = loanTypeUseCase.listLoanTypes()
                .map(loanTypeWebMapper::toResponse);
        return Mono.just(ResponseEntity.ok(flux));
    }

    @Operation(summary = "Get a loan type by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Loan type found",
                            content = @Content(schema = @Schema(implementation = LoanTypeResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Loan type not found")
            })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<LoanTypeResponseDto>> getLoanTypeById(
            @Parameter(description = "ID of the loan type", required = true)
            @PathVariable("id") Integer id) {
        return loanTypeUseCase.getLoanTypeById(id)
                .map(loanType -> ResponseEntity.ok(loanTypeWebMapper.toResponse(loanType)));
    }

    @Operation(summary = "Update a loan type by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Loan type updated",
                            content = @Content(schema = @Schema(implementation = LoanTypeResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Loan type not found")
            })
    @PutMapping("/{id}")
    public Mono<ResponseEntity<LoanTypeResponseDto>> updateLoanType(
            @Parameter(description = "ID of the loan type", required = true)
            @PathVariable("id") Integer id,
            @Valid @RequestBody LoanTypeRequestDto dto) {

        return loanTypeUseCase.updateLoanType(id, loanTypeWebMapper.toDomain(dto))
                .map(updated -> ResponseEntity.ok(loanTypeWebMapper.toResponse(updated)));
    }

    @Operation(summary = "Delete a loan type by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Loan type deleted"),
                    @ApiResponse(responseCode = "404", description = "Loan type not found")
            })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<String>> deleteLoanType(
            @Parameter(description = "ID of the loan type", required = true)
            @PathVariable("id") Integer id) {

        return loanTypeUseCase.deleteLoanType(id)
                .then(Mono.just(ResponseEntity.ok("Tipo de préstamo eliminado correctamente.")))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(404).body(e.getMessage())));
    }
}
