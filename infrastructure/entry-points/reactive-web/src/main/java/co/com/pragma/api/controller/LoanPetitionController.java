package co.com.pragma.api.controller;

import co.com.pragma.api.dto.request.LoanPetitionRequestDto;
import co.com.pragma.api.dto.response.LoanPetitionResponseDto;
import co.com.pragma.api.mapper.LoanPetitionWebMapper;
import co.com.pragma.usecase.loanpetition.LoanPetitionUseCase;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/loan-petitions")
@RequiredArgsConstructor
@Tag(name = "Loan Petitions", description = "APIs for managing loan petitions")
public class LoanPetitionController {

    private final LoanPetitionUseCase loanPetitionUseCase;
    private final LoanPetitionWebMapper loanPetitionWebMapper;

    @Operation(summary = "Create a new loan petition",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Loan petition created successfully",
                            content = @Content(schema = @Schema(implementation = LoanPetitionResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<LoanPetitionResponseDto> createLoanPetition(
            @Valid @RequestBody LoanPetitionRequestDto dto) {
        return loanPetitionUseCase.createLoanPetition(loanPetitionWebMapper.toDomain(dto))
                .map(loanPetitionWebMapper::toResponse);
    }

    @Operation(summary = "Find loan petition by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Loan petition found",
                            content = @Content(schema = @Schema(implementation = LoanPetitionResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid UUID format"),
                    @ApiResponse(responseCode = "404", description = "Loan petition not found")
            })
    @GetMapping("/{id}")
    public Mono<LoanPetitionResponseDto> findByIdLoanPetition(
            @Parameter(description = "UUID of the loan petition", required = true)
            @PathVariable("id") String id) {
        UUID loanPetitionId;
        try {
            loanPetitionId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return Mono.error(new IllegalArgumentException("Invalid UUID format."));
        }
        return loanPetitionUseCase.findByIdLoanPetition(loanPetitionId)
                .map(loanPetitionWebMapper::toResponse);
    }

    @Operation(summary = "List all loan petitions",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of loan petitions",
                            content = @Content(schema = @Schema(implementation = LoanPetitionResponseDto.class)))
            })
    @GetMapping
    public Flux<LoanPetitionResponseDto> listAllLoanPetition() {
        return loanPetitionUseCase.listAllLoanPetition()
                .map(loanPetitionWebMapper::toResponse);
    }

    @Operation(summary = "Update a loan petition by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Loan petition updated successfully",
                            content = @Content(schema = @Schema(implementation = LoanPetitionResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid UUID or request data"),
                    @ApiResponse(responseCode = "404", description = "Loan petition not found")
            })
    @PutMapping("/{id}")
    public Mono<LoanPetitionResponseDto> updateLoanPetition(
            @Parameter(description = "UUID of the loan petition", required = true)
            @PathVariable("id") String id,
            @Valid @RequestBody LoanPetitionRequestDto requestDto) {

        UUID loanPetitionId;
        try {
            loanPetitionId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return Mono.error(new IllegalArgumentException("Invalid UUID format."));
        }

        return loanPetitionUseCase.updateLoanPetition(loanPetitionId, loanPetitionWebMapper.toDomain(requestDto))
                .map(loanPetitionWebMapper::toResponse);
    }

    @Operation(summary = "Delete a loan petition by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Loan petition deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid UUID format"),
                    @ApiResponse(responseCode = "404", description = "Loan petition not found")
            })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<String>> deleteLoanPetition(
            @Parameter(description = "UUID of the loan petition", required = true)
            @PathVariable("id") String id) {

        UUID loanPetitionId;
        try {
            loanPetitionId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return Mono.error(new IllegalArgumentException("Invalid UUID format."));
        }

        return loanPetitionUseCase.deleteLoanPetition(loanPetitionId)
                .then(Mono.just(ResponseEntity.ok("La solicitud de préstamo se eliminó correctamente.")))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(404).body(e.getMessage())));
    }
}
