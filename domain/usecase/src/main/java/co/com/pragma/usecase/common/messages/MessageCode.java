package co.com.pragma.usecase.common.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageCode {
    // ===== Estado =====
    STATE_NAME_REQUIRED("v0001", "El nombre del estado es obligatorio.", 400),
    STATE_ALREADY_EXISTS("v0002", "El estado ya existe.", 409),
    STATE_NOT_FOUND_BY_ID("v0003", "El estado con id {0} no existe.", 404),

    // ===== Tipo de préstamo =====
    LOAN_TYPE_NAME_REQUIRED("v0004", "El nombre del tipo de préstamo es obligatorio.", 400),
    LOAN_TYPE_MIN_AMOUNT_REQUIRED("v0005", "El monto mínimo es obligatorio.", 400),
    LOAN_TYPE_MIN_AMOUNT_INVALID("v0006", "El monto mínimo debe ser mayor o igual a 0.", 400),
    LOAN_TYPE_MAX_AMOUNT_REQUIRED("v0007", "El monto máximo es obligatorio.", 400),
    LOAN_TYPE_MAX_AMOUNT_INVALID("v0008", "El monto máximo debe ser mayor o igual al mínimo.", 400),
    LOAN_TYPE_INTEREST_RATE_REQUIRED("v0009", "La tasa de interés es obligatoria.", 400),
    LOAN_TYPE_INTEREST_RATE_INVALID("v0010", "La tasa de interés debe ser mayor o igual a 0.", 400),
    LOAN_TYPE_ALREADY_EXISTS("v0011", "El tipo de préstamo ya existe.", 409),
    LOAN_TYPE_NOT_FOUND_BY_ID("v0012", "El tipo de préstamo con id {0} no existe.", 404),

    // ===== Solicitud de préstamo =====
    LOAN_PETITION_DOCUMENT_ID_REQUIRED("v0013", "El documentId es obligatorio.", 400),
    LOAN_PETITION_TYPE_ID_REQUIRED("v0014", "El loanTypeId es obligatorio.", 400),
    LOAN_PETITION_PENDING_REVIEW("v0015", "Pendiente de revisión.", 400),
    LOAN_PETITION_INITIAL_STATE_NOT_FOUND("v0016", "El estado inicial 'Pendiente de revisión' no existe en la BD.", 400),
    LOAN_PETITION_TYPE_NOT_FOUND("v0017", "El tipo de préstamo no existe.", 400),
    LOAN_PETITION_NOT_FOUND("v0018", "Solicitud de préstamo no encontrada.", 404),
    LOAN_PETITION_STATE_NOT_FOUND("v0019", "El estado no existe.", 400),

    // ===== Request general =====
    REQUEST_BODY_EMPTY("v0020", "El body no puede estar vacío.", 400),

    USER_NOT_AUTHORIZED("v0021", "El documentId no corresponde al cliente logueado.", 404),
    UNAUTHORIZED("v0022", "El usuario no es un cliente", 400);
    private final String errorCode;
    private final String message;
    private final Integer httpStatus;
}
