package co.com.pragma.api.router;

import co.com.pragma.api.handler.ListLoanReviewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class ListLoanReviewRouter {

    @Bean
    public RouterFunction<ServerResponse> loanReviewRoutes(ListLoanReviewHandler handler) {
        return route(GET("/api/v1/loan-reviews"), handler::listLoanReviews);
    }
}
