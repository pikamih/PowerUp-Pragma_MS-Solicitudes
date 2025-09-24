package co.com.pragma.consumer.config;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
@Configuration
public class RestConsumerConfig {

    private final String url;

    private final int timeout;

    public RestConsumerConfig(@Value("${adapter.restconsumer.url}") String url,
                              @Value("${adapter.restconsumer.timeout}") int timeout) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("adapter.restconsumer.url no puede estar vacío");
        }
            this.url = url;
        this.timeout = timeout;
    }

    @Bean
    public WebClient getWebClient() {

        return WebClient.builder()
            .baseUrl(url)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .filter(authHeaderFilter())
            .clientConnector(getClientHttpConnector())
            .build();
    }

    private ClientHttpConnector getClientHttpConnector() {
        /*
        IF YO REQUIRE APPEND SSL CERTIFICATE SELF SIGNED: this should be in the default cacerts trustore
        */
        return new ReactorClientHttpConnector(HttpClient.create()
                .compress(true)
                .keepAlive(true)
                .option(CONNECT_TIMEOUT_MILLIS, timeout)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(timeout, MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(timeout, MILLISECONDS));
                }));
    }

    private ExchangeFilterFunction authHeaderFilter() {
        return (request, next) ->
                ReactiveSecurityContextHolder.getContext()
                        .doOnNext(ctx -> log.info("SecurityContext en WebClient: {}", ctx))
                        .map(ctx -> ctx.getAuthentication())
                        .doOnNext(auth -> log.info("Authentication en WebClient: {}", auth))
                        .filter(auth -> auth != null && auth.getCredentials() != null)
                        .flatMap(auth -> {
                            String jwt = auth.getCredentials().toString();
                            log.info("Propagando JWT: {}", jwt);
                            return next.exchange(
                                    ClientRequest.from(request)
                                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                                            .build()
                            );
                        })
                        .switchIfEmpty(next.exchange(request));
    }


}
