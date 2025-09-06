package co.com.pragma.messagetranslator;

import co.com.pragma.usecase.common.messages.MessageCode;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageTranslator {

    private final MessageSource messageSource;

    public MessageTranslator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String translate(MessageCode code, Object... params) {
        return messageSource.getMessage(code.name(), params, Locale.getDefault());
    }
}
