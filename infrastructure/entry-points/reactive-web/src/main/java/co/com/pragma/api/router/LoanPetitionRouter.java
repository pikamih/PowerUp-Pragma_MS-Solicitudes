package co.com.pragma.api.router;

import co.com.pragma.api.handler.LoanPetitionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoanPetitionRouter {

    @Bean
    public RouterFunction<ServerResponse> loanPetitionRoutes(LoanPetitionHandler handler) {
        return route(POST("/api/v1/loan-petitions"), handler::createLoanPetition)
                .andRoute(GET("/api/v1/loan-petitions/{id}"), handler::findByIdLoanPetition)
                .andRoute(GET("/api/v1/loan-petitions"), handler::listAllLoanPetition)
                .andRoute(PUT("/api/v1/loan-petitions/{id}"), handler::updateLoanPetition)
                .andRoute(DELETE("/api/v1/loan-petitions/{id}"), handler::deleteLoanPetition);
    }
}
