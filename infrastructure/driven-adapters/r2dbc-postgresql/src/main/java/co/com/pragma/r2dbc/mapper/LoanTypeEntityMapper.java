package co.com.pragma.r2dbc.mapper;


import co.com.pragma.model.loantype.LoanType;
import co.com.pragma.r2dbc.entity.LoanTypeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanTypeEntityMapper {

    LoanType toDomain(LoanTypeEntity entity);

    LoanTypeEntity toEntity(LoanType loanType);
}
