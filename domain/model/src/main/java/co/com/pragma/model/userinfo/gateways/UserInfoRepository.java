package co.com.pragma.model.userinfo.gateways;

import co.com.pragma.model.userinfo.UserInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserInfoRepository {

    Flux<UserInfo> findByDocumentId(String documentId);

}
