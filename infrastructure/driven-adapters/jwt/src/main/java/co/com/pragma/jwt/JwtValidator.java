package co.com.pragma.jwt;

import co.com.pragma.jwt.adapter.PemKeys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.PublicKey;

@Component
public class JwtValidator {

    private final PublicKey publicKey;

    public JwtValidator(@Value("${security.jwt.public-key-path}") String publicKeyPath) throws Exception {
        this.publicKey = PemKeys.readPublicKey(publicKeyPath);
    }

    public Mono<Claims> validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Mono.just(claims);
        } catch (JwtException e) {
            return Mono.error(new RuntimeException("Token inválido o expirado", e));
        }
    }
}
