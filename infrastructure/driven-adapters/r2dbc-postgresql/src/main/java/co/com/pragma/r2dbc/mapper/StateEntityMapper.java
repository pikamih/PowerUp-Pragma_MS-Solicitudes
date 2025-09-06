package co.com.pragma.r2dbc.mapper;

import co.com.pragma.model.state.State;
import co.com.pragma.r2dbc.entity.StateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StateEntityMapper {

    State toDomain(StateEntity entity);

    StateEntity toEntity(State state);

}
