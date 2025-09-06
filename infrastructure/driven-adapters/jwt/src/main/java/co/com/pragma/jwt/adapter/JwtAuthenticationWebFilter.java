package co.com.pragma.jwt.adapter;

import co.com.pragma.jwt.JwtValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class JwtAuthenticationWebFilter implements WebFilter {

    private final JwtValidator jwtValidator;

    public JwtAuthenticationWebFilter(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.web.server.WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            return jwtValidator.validateToken(token) // devuelve Mono<Claims>
                    .flatMap(claims -> {
                        var auth = new UsernamePasswordAuthenticationToken(
                                claims.getSubject(), // email o username
                                null,
                                Collections.singleton(() -> "ROLE_" + claims.get("role"))
                        );
                        return chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                    })
                    .onErrorResume(e -> { // token inválido
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        }

        return chain.filter(exchange);
    }
}
