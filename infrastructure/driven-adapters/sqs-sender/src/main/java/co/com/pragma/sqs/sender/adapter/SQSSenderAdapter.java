package co.com.pragma.sqs.sender.adapter;

import co.com.pragma.model.loanpetition.gateways.LoanNotificationGateway;
import co.com.pragma.sqs.sender.SQSSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SQSSenderAdapter implements LoanNotificationGateway {
    private final SQSSender sqsSender;

    @Override
    public Mono<String> sendMessageSQS(String message) {
        return sqsSender.send(message);
    }
}
