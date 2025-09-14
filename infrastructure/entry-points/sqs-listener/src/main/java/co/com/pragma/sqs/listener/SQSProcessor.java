package co.com.pragma.sqs.listener;

import co.com.pragma.model.loanpetition.LoanDecision;
import co.com.pragma.usecase.loanpetition.LoanPetitionUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.UUID;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {
    private final ObjectMapper objectMapper;
    private final LoanPetitionUseCase loanPetitionUseCase;

    @Override
    public Mono<Void> apply(Message message) {
        try {
            // Parsear JSON del mensaje SQS
            LoanDecision decision = objectMapper.readValue(message.body(), LoanDecision.class);

            return loanPetitionUseCase.updateLoanStateByDecision(
                    UUID.fromString(decision.getLoanId()),
                    decision.getDecision()
            ).then();

        } catch (JsonProcessingException e) {
            log.error("Error parsing SQS message: {}", message.body(), e);
            return Mono.error(e);
        }

    }
}
