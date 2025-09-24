package co.com.pragma.model.userinfo.gateways;

import co.com.pragma.model.userinfo.UserInfo;
import reactor.core.publisher.Mono;

public interface UserInfoRepository {

    Mono<UserInfo> findByDocumentId(String documentId);

}
