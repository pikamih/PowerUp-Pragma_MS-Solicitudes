package co.com.pragma.api;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;

@ContextConfiguration(classes = {RouterRestTest.TestConfig.class, RouterRest.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private Handler handler;

    @Configuration
    static class TestConfig {
        @Bean
        Handler handler() {
            return Mockito.mock(Handler.class);
        }
    }

    @Test
    void testListenGETUseCase() {
        Mockito.when(handler.listenGETUseCase(Mockito.any()))
                .thenReturn(ServerResponse.ok().bodyValue("GET UseCase Response"));

        webTestClient.get()
                .uri("/api/usecase/path")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> org.assertj.core.api.Assertions.assertThat(response)
                        .isEqualTo("GET UseCase Response"));
    }

    @Test
    void testListenGETOtherUseCase() {
        Mockito.when(handler.listenGETOtherUseCase(Mockito.any()))
                .thenReturn(ServerResponse.ok().bodyValue("GET OtherUseCase Response"));

        webTestClient.get()
                .uri("/api/otherusercase/path")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> org.assertj.core.api.Assertions.assertThat(response)
                        .isEqualTo("GET OtherUseCase Response"));
    }

    @Test
    void testListenPOSTUseCase() {
        Mockito.when(handler.listenPOSTUseCase(Mockito.any()))
                .thenReturn(ServerResponse.ok().bodyValue("POST UseCase Response"));

        webTestClient.post()
                .uri("/api/usecase/otherpath")
                .bodyValue("")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> org.assertj.core.api.Assertions.assertThat(response)
                        .isEqualTo("POST UseCase Response"));
    }
}
