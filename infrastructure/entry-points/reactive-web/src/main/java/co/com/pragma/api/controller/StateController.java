package co.com.pragma.api.controller;

import co.com.pragma.api.dto.request.StateRequestDto;
import co.com.pragma.api.dto.response.StateResponseDto;
import co.com.pragma.api.mapper.StateWebMapper;
import co.com.pragma.usecase.state.StateUseCase;
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
@RequestMapping("/api/v1/states")
@RequiredArgsConstructor
@Tag(name = "States", description = "APIs for managing states")
public class StateController {

    private final StateUseCase stateUseCase;
    private final StateWebMapper stateWebMapper;

    @Operation(summary = "Create a new state",
            responses = {
                    @ApiResponse(responseCode = "201", description = "State created",
                            content = @Content(schema = @Schema(implementation = StateResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<StateResponseDto> createState(@Valid @RequestBody StateRequestDto dto) {
        return stateUseCase.createState(stateWebMapper.toDomain(dto))
                .map(stateWebMapper::toResponse);
    }

    @Operation(summary = "Get all states",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of states",
                            content = @Content(schema = @Schema(implementation = StateResponseDto.class)))
            })
    @GetMapping
    public Mono<ResponseEntity<Flux<StateResponseDto>>> getAllState() {
        Flux<StateResponseDto> stateFlux = stateUseCase.listStates()
                .map(stateWebMapper::toResponse);
        return Mono.just(ResponseEntity.ok(stateFlux));
    }

    @Operation(summary = "Get a state by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "State found",
                            content = @Content(schema = @Schema(implementation = StateResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "State not found")
            })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<StateResponseDto>> getStateById(
            @Parameter(description = "ID of the state", required = true)
            @PathVariable("id") Integer id) {
        return stateUseCase.getStateById(id)
                .map(state -> ResponseEntity.ok(stateWebMapper.toResponse(state)));
    }

    @Operation(summary = "Update a state by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "State updated",
                            content = @Content(schema = @Schema(implementation = StateResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "State not found")
            })
    @PutMapping("/{id}")
    public Mono<ResponseEntity<StateResponseDto>> updateState(
            @Parameter(description = "ID of the state", required = true)
            @PathVariable("id") Integer id,
            @Valid @RequestBody StateRequestDto dto) {

        return stateUseCase.updateState(id, stateWebMapper.toDomain(dto))
                .map(updateState -> ResponseEntity.ok(stateWebMapper.toResponse(updateState)));
    }

    @Operation(summary = "Delete a state by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "State deleted"),
                    @ApiResponse(responseCode = "404", description = "State not found")
            })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<String>> deleteState(
            @Parameter(description = "ID of the state", required = true)
            @PathVariable("id") Integer id) {

        return stateUseCase.deleteState(id)
                .then(Mono.just(ResponseEntity.ok("Estado eliminado correctamente.")))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(404).body(e.getMessage())));
    }
}
