package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.request.LoanPetitionRequestDto;
import co.com.pragma.api.dto.response.LoanPetitionResponseDto;
import co.com.pragma.model.loanpetition.LoanPetition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface LoanPetitionWebMapper {

    // Request DTO -> Domain
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stateId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LoanPetition toDomain(LoanPetitionRequestDto dto);

    // Domain -> Response DTO
    LoanPetitionResponseDto toResponse(LoanPetition domain);

}
