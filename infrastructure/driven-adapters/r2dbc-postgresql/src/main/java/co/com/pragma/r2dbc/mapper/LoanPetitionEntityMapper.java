package co.com.pragma.r2dbc.mapper;

import co.com.pragma.model.loanpetition.LoanPetition;
import co.com.pragma.r2dbc.entity.LoanPetitionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanPetitionEntityMapper {

    LoanPetition toDomain(LoanPetitionEntity entity);

    LoanPetitionEntity toEntity(LoanPetition loanPetition);
}
