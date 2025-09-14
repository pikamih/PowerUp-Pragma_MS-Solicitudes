package co.com.pragma.sqs.listener.helper;

import co.com.pragma.sqs.listener.config.SQSProperties;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

@Log4j2
@Builder
public class SQSListener {
    private final SqsAsyncClient client;
    private final SQSProperties properties;
    private final Function<Message, Mono<Void>> processor;
    private String operation;

    public SQSListener start() {
        this.operation = "MessageFrom:" + properties.queueUrl();

        var scheduler = Schedulers.fromExecutorService(
                Executors.newFixedThreadPool(properties.numberOfThreads())
        );

        listenRetryRepeat()
                .flatMap(message -> processor.apply(message)
                        .then(confirm(message)), properties.numberOfThreads())
                .subscribeOn(scheduler)
                .subscribe(
                        null,
                        e -> log.error("Error global en SQSListener", e)
                );

        return this;
    }



    private Flux<Message> listenRetryRepeat() {
        return getMessages()
                .doOnNext(message -> log.debug("Mensaje recibido: {}", message.messageId()))
                .doOnError(e -> log.error("Error listening sqs queue", e))
                .repeat();
    }


    private Flux<Void> listen() {
        return getMessages()
                .flatMap(message -> processor.apply(message)
                        .name("async_operation")
                        .tag("operation", operation)
                        .metrics()
                        .then(confirm(message)))
                .onErrorContinue((e, o) -> log.error("Error listening sqs message", e));
    }

    private Mono<Void> confirm(Message message) {
        return Mono.fromCallable(() -> getDeleteMessageRequest(message.receiptHandle()))
                .flatMap(request -> Mono.fromFuture(client.deleteMessage(request)))
                .then();
    }

    private Flux<Message> getMessages() {
        return Mono.fromCallable(this::getReceiveMessageRequest)
                .flatMap(request -> Mono.fromFuture(client.receiveMessage(request)))
                .doOnNext(response -> log.debug("{} received messages from sqs", response.messages().size()))
                .flatMapMany(response -> Flux.fromIterable(response.messages()));
    }

    private ReceiveMessageRequest getReceiveMessageRequest() {
        return ReceiveMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .maxNumberOfMessages(properties.maxNumberOfMessages())
                .waitTimeSeconds(properties.waitTimeSeconds())
                .visibilityTimeout(properties.visibilityTimeoutSeconds())
                .build();
    }

    private DeleteMessageRequest getDeleteMessageRequest(String receiptHandle) {
        return DeleteMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .receiptHandle(receiptHandle)
                .build();
    }
}
