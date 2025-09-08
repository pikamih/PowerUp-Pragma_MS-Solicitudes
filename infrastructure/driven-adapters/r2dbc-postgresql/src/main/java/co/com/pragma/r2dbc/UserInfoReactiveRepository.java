package co.com.pragma.r2dbc;

import co.com.pragma.model.userinfo.gateways.UserInfoRepository;
import co.com.pragma.r2dbc.entity.UserInfoEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserInfoReactiveRepository extends ReactiveCrudRepository<UserInfoEntity, UUID> {
    Flux<UserInfoEntity> findByDocumentId(String documentId);
}

