package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.request.LoanTypeRequestDto;
import co.com.pragma.api.dto.response.LoanTypeResponseDto;
import co.com.pragma.model.loantype.LoanType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface LoanTypeWebMapper {

    @Mapping(target = "id", ignore = true)
    LoanType toDomain(LoanTypeRequestDto dto);

    LoanTypeResponseDto toResponse(LoanType loanType);



}
