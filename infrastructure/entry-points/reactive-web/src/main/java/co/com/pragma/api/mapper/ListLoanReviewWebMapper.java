package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.request.LoanDecisionRequest;
import co.com.pragma.api.dto.response.ListLoanReviewResponseDto;
import co.com.pragma.api.dto.response.LoanDecisionResponse;
import co.com.pragma.model.loanpetition.LoanDecision;
import co.com.pragma.model.loanpetition.LoanPetition;
import co.com.pragma.model.loanpetition.LoanReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ListLoanReviewWebMapper {

    ListLoanReviewResponseDto toResponseDto(LoanReview loanReview);

    // Request → Domain
    LoanDecision toDomain(LoanDecisionRequest request, String loanId);

    @Mapping(target = "loanId", source = "loanPetition.id")
    @Mapping(target = "decision", source = "stateName")
    LoanDecisionResponse toResponseDto(LoanPetition loanPetition, String stateName);

}
