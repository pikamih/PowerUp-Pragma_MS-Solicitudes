package co.com.pragma.api.config;

import co.com.pragma.api.dto.response.ErrorResponse;
import co.com.pragma.messagetranslator.MessageTranslator;
import co.com.pragma.usecase.common.messages.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageTranslator translator;

    public GlobalExceptionHandler(MessageTranslator translator) {
        this.translator = translator;
    }


    /**
     * Maneja errores cuando el request está mal formado
     * (ej: id no es número, parámetros inválidos, etc.)
     */
    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleServerWebInput(ServerWebInputException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        ex.getReason(),
                        HttpStatus.BAD_REQUEST.value(),
                        "",
                        LocalDateTime.now())));
    }

    /**
     * Manejo genérico para RuntimeException no controladas (500)
     */
    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleRuntimeException(RuntimeException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Error interno en el servidor",
                        LocalDateTime.now())));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        HttpStatus status =  HttpStatus.valueOf(ex.getCode().getHttpStatus());


        // Traduce el mensaje usando el MessageTranslator y los parámetros
        String message = translator.translate(ex.getCode(), ex.getParams());
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(
                        message,
                        status.value(),
                        status.getReasonPhrase(),
                        LocalDateTime.now()
                ));
    }


}
