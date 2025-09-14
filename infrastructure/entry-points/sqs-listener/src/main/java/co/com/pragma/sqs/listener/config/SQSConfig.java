package co.com.pragma.sqs.listener.config;

import co.com.pragma.sqs.listener.helper.SQSListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.metrics.MetricPublisher;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.Message;

import java.net.URI;
import java.util.function.Function;

@Configuration
@ConditionalOnMissingBean(SqsAsyncClient.class)
public class SQSConfig {

    @Bean
    public SQSListener sqsListener(SqsAsyncClient client, SQSProperties properties, Function<Message, Mono<Void>> fn) {
        return SQSListener.builder()
                .client(client)
                .properties(properties)
                .processor(fn)
                .build()
                .start();
    }

    @Bean
    public SqsAsyncClient configSqs(SQSProperties properties, MetricPublisher publisher) {

        String region = System.getenv("AWS_REGION");
        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
        String endpoint = System.getenv("SQS_ENDPOINT_LISTENER");



        return SqsAsyncClient.builder()
                .endpointOverride(endpoint != null ? URI.create(endpoint) : null)
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .build();
    }

    private AwsCredentialsProviderChain getProviderChain() {
        return AwsCredentialsProviderChain.builder()
                .addCredentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .addCredentialsProvider(SystemPropertyCredentialsProvider.create())
                .addCredentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                .addCredentialsProvider(ProfileCredentialsProvider.create())
                .addCredentialsProvider(ContainerCredentialsProvider.builder().build())
                .addCredentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();
    }

    protected URI resolveEndpoint(SQSProperties properties) {
        if (properties.endpoint() != null) {
            return URI.create(properties.endpoint());
        }
        return null;
    }
}
