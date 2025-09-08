package co.com.pragma.r2dbc.mapper;

import co.com.pragma.model.userinfo.UserInfo;
import co.com.pragma.r2dbc.entity.UserInfoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserInfoEntityMapper {

    UserInfo toDomain(UserInfoEntity entity);

}
