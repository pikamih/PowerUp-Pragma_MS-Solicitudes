package co.com.pragma.usecase.common.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageCode {
    // ===== State =====
    STATE_NAME_REQUIRED("v0001", "State name is required.", 400),
    STATE_ALREADY_EXISTS("v0002", "State already exists.", 409),
    STATE_NOT_FOUND_BY_ID("v0003", "State with ID {0} does not exist.", 404),

    // ===== Loan type =====
    LOAN_TYPE_NAME_REQUIRED("v0004", "Loan type name is required.", 400),
    LOAN_TYPE_MIN_AMOUNT_REQUIRED("v0005", "Minimum amount is required.", 400),
    LOAN_TYPE_MIN_AMOUNT_INVALID("v0006", "Minimum amount must be greater than or equal to 0.", 400),
    LOAN_TYPE_MAX_AMOUNT_REQUIRED("v0007", "Maximum amount is required.", 400),
    LOAN_TYPE_MAX_AMOUNT_INVALID("v0008", "Maximum amount must be greater than or equal to the minimum.", 400),
    LOAN_TYPE_INTEREST_RATE_REQUIRED("v0009", "Interest rate is required.", 400),
    LOAN_TYPE_INTEREST_RATE_INVALID("v0010", "Interest rate must be greater than or equal to 0.", 400),
    LOAN_TYPE_ALREADY_EXISTS("v0011", "Loan type already exists.", 409),
    LOAN_TYPE_NOT_FOUND_BY_ID("v0012", "Loan type with ID {0} does not exist.", 404),

    // ===== Loan petition =====
    LOAN_PETITION_DOCUMENT_ID_REQUIRED("v0013", "Document ID is required.", 400),
    LOAN_PETITION_TYPE_ID_REQUIRED("v0014", "Loan type ID is required.", 400),
    LOAN_PETITION_PENDING_REVIEW("v0015", "Pending review.", 400),
    LOAN_PETITION_INITIAL_STATE_NOT_FOUND("v0016", "Initial state 'Pending review' does not exist in the database.", 400),
    LOAN_PETITION_TYPE_NOT_FOUND("v0017", "Loan type does not exist.", 400),
    LOAN_PETITION_NOT_FOUND("v0018", "Loan petition not found.", 404),
    LOAN_PETITION_STATE_NOT_FOUND("v0019", "State does not exist.", 400),

    // ===== Request general =====
    REQUEST_BODY_EMPTY("v0020", "Request body cannot be empty.", 400),

    USER_NOT_AUTHORIZED("v0021", "Document ID does not belong to the logged-in client.", 404),
    CLIENT_ROLE_UNAUTHORIZED("v0022", "The user role is not a CLIENTE.", 400),
    ASESOR_ROLE_UNAUTHORIZED("v0022", "The user role is not a ASESOR.", 400);

    private final String errorCode;
    private final String message;
    private final Integer httpStatus;
}
