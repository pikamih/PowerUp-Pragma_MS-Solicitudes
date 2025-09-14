package co.com.pragma.model.loanpetition.gateways;

import co.com.pragma.model.loanpetition.LoanPetition;
import co.com.pragma.model.loanpetition.LoanReview;
import reactor.core.publisher.Flux;

public interface LoanReviewRepository {

    Flux<LoanReview> mapToLoanReview (LoanPetition petition);
}
