package co.com.pragma.usecase.common.messages;

import lombok.Getter;


@Getter
public class BusinessException extends RuntimeException {
    private final MessageCode code;
    private final Object[] params;

    public BusinessException(MessageCode code, Object[] params) {
      super(code.name());
        this.code = code;
        this.params = params;
    }

}
