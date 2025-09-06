package co.com.pragma.api.router;

import co.com.pragma.api.handler.LoanTypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoanTypeRouter {

    @Bean
    public RouterFunction<ServerResponse> loanTypeRoutes(LoanTypeHandler handler) {
        return route(POST("/api/v1/loan-types"), handler::createLoanType)
                .andRoute(GET("/api/v1/loan-types"), handler::listLoanTypes)
                .andRoute(GET("/api/v1/loan-types/{id}"), handler::getLoanTypeById)
                .andRoute(PUT("/api/v1/loan-types/{id}"), handler::updateLoanType)
                .andRoute(DELETE("/api/v1/loan-types/{id}"), handler::deleteLoanType);
    }
}
