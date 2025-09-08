package co.com.pragma.r2dbc.adapter;

import co.com.pragma.model.userinfo.UserInfo;
import co.com.pragma.model.userinfo.gateways.UserInfoRepository;
import co.com.pragma.r2dbc.UserInfoReactiveRepository;
import co.com.pragma.r2dbc.mapper.UserInfoEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserInfoReactiveRepositoryAdapter implements UserInfoRepository {

    private final UserInfoReactiveRepository reactiveRepository;
    private final UserInfoEntityMapper userInfoEntityMapper;

    @Override
    public Flux<UserInfo> findByDocumentId(String documentId) {
        return reactiveRepository.findByDocumentId(documentId)
                .map(userInfoEntityMapper::toDomain);
    }

}
