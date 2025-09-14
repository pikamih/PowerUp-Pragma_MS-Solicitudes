package co.com.pragma.config;

import co.com.pragma.model.loanpetition.gateways.LoanNotificationGateway;
import co.com.pragma.model.loanpetition.gateways.LoanPetitionRepository;
import co.com.pragma.model.loanpetition.gateways.LoanReviewRepository;
import co.com.pragma.model.loantype.gateways.LoanTypeRepository;
import co.com.pragma.model.state.gateways.StateRepository;
import co.com.pragma.model.userinfo.gateways.UserInfoRepository;
import co.com.pragma.usecase.loanpetition.ListLoanReviewsUseCase;
import co.com.pragma.usecase.loanpetition.LoanPetitionUseCase;
import co.com.pragma.usecase.loantype.LoanTypeUseCase;
import co.com.pragma.usecase.state.StateUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "co.com.pragma.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = org.springframework.stereotype.Component.class)
        },
        useDefaultFilters = false
)
public class UseCasesConfig {

        private final StateRepository stateRepository;
        private final LoanTypeRepository loanTypeRepository;
        private final LoanPetitionRepository loanPetitionRepository;
        private final LoanNotificationGateway notificationGateway;
        private final LoanReviewRepository loanReviewRepository;

    public UseCasesConfig(StateRepository stateRepository, LoanTypeRepository loanTypeRepository, LoanPetitionRepository loanPetitionRepository, LoanNotificationGateway notificationGateway, LoanReviewRepository loanReviewRepository) {
        this.stateRepository = stateRepository;
        this.loanTypeRepository = loanTypeRepository;
        this.loanPetitionRepository = loanPetitionRepository;
        this.notificationGateway = notificationGateway;
        this.loanReviewRepository = loanReviewRepository;
    }


    @Bean
    public StateUseCase stateUseCase(){
            return new StateUseCase(stateRepository);
    }

    @Bean
    public LoanTypeUseCase loanTypeUseCase(){
        return new LoanTypeUseCase(loanTypeRepository);
    }

    @Bean
    public LoanPetitionUseCase loanPetitionUseCase(){
        return new LoanPetitionUseCase(loanPetitionRepository, loanTypeRepository, stateRepository, notificationGateway, loanReviewRepository);
    }

    @Bean
    public ListLoanReviewsUseCase listLoanReviewsUseCase(){
        return new ListLoanReviewsUseCase(loanPetitionRepository, stateRepository, notificationGateway, loanReviewRepository);
    }
}
