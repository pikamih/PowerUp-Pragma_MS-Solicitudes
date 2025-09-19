package co.com.pragma.consumer;

import co.com.pragma.consumer.dto.UserInfoResponse;
import co.com.pragma.model.userinfo.UserInfo;
import co.com.pragma.model.userinfo.gateways.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInfoRestAdapter implements UserInfoRepository {

    private final WebClient client;

    @Override
    public Mono<UserInfo> findByDocumentId(String documentId) {

        return client
                .get()
                .uri("/api/v1/users/{id}", documentId)
                .retrieve()
                .bodyToMono(UserInfoResponse.class)
                .map(this::toDomain);
    }

    private UserInfo toDomain(UserInfoResponse response) {
        return UserInfo.builder()
                .documentId(response.getDocumentId())
                .firstName(response.getFirstName())
                .lastName(response.getLastName())
                .email(response.getEmail())
                .baseSalary(response.getBaseSalary())
                .build();
    }

}
