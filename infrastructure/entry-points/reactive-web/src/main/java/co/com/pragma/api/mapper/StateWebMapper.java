package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.request.StateRequestDto;
import co.com.pragma.api.dto.response.StateResponseDto;
import co.com.pragma.model.state.State;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface StateWebMapper {

    // Request DTO -> Domain
    @Mapping(target = "id", ignore = true)
    State toDomain(StateRequestDto dto);

    // Domain -> Response DTO

    StateResponseDto toResponse(State state);
}
