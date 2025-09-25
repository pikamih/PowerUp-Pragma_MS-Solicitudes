package co.com.pragma.api.router;

import co.com.pragma.api.handler.StateHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class StateRouter {

    @Bean
    public RouterFunction<ServerResponse> stateRoutes(StateHandler handler) {
        return route(POST("/api/v1/loans/states"), handler::createState)
                .andRoute(GET("/api/v1/loans/states/{id}"), handler::getStateById)
                .andRoute(GET("/api/v1/loans/states"), handler::listState)
                .andRoute(PUT("/api/v1/loans/states/{id}"), handler::updateState)
                .andRoute(DELETE("/api/v1/loans/states/{id}"), handler::deleteState);
    }
}
