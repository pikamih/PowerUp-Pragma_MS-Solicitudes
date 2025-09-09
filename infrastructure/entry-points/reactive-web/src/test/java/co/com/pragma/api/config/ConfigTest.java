package co.com.pragma.api.config;

import co.com.pragma.api.Handler;
import co.com.pragma.api.RouterRest;
import co.com.pragma.jwt.JwtValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
@Import({
        CorsConfig.class,
        SecurityHeadersConfig.class,
        ConfigTest.JwtServiceTestConfig.class,
        ConfigTest.TestSecurityConfig.class  // <-- Agregamos seguridad de prueba
})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JwtValidator jwtValidator;

    @Test
    void corsConfigurationShouldAllowOrigins() {
        webTestClient.get()
                .uri("/api/usecase/path")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

    @TestConfiguration
    static class JwtServiceTestConfig {
        @Bean
        public JwtValidator jwtService() {
            JwtValidator mock = Mockito.mock(JwtValidator.class);
            Mockito.when(mock.validateToken(Mockito.any()))
                    .thenAnswer(invocation -> Mono.just("dummy-token"));
            Mockito.when(mock.validateToken(Mockito.any()))
                    .thenAnswer(invocation ->Mono.empty());
            return mock;
        }
    }


    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
            http
                    .authorizeExchange(ex -> ex.anyExchange().permitAll()) // permite todo
                    .csrf(csrf -> csrf.disable()); // deshabilita CSRF
            return http.build();
        }
    }
}
