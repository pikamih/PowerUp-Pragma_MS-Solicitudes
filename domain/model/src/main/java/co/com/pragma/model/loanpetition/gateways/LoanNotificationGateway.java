package co.com.pragma.model.loanpetition.gateways;

import reactor.core.publisher.Mono;

public interface LoanNotificationGateway {
    Mono<String> sendMessageSQS(String message);
}
