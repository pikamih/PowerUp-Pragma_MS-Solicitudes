package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.response.ListLoanReviewResponseDto;
import co.com.pragma.model.loanpetition.LoanReview;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ListLoanReviewWebMapper {

    ListLoanReviewResponseDto toResponseDto(LoanReview loanReview);

}
