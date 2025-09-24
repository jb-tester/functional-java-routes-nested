package com.mytests.spring.functionaljavaroutesnested;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class NestedRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> nestedRouterFunction() {

        return  RouterFunctions
                .nest(path("/route_nested/level1"),
                        route(GET("/route1"), this::getRoute1) // correct request is generated
                                .andRoute(GET("/route2"), this::getRoute2)
                                .andNest(path("/level2"),
                                        route(GET("/route21"), this::getRoute21) // incorrect request is generated: `/level2` fragment is missing
                                                .andRoute(GET("/route22"), this::getRoute22)
                                                .andRoute(POST("/route22"), this::postRoute22)
                                ));

    }

    private Mono<ServerResponse> postRoute22(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(String.class)
                .flatMap(body -> ServerResponse.ok().bodyValue("postRoute22 body:"+ body));
    }

    private Mono<ServerResponse> getRoute22(ServerRequest serverRequest) {
        return ServerResponse.ok().body(fromValue("getRoute22"));
    }

    private Mono<ServerResponse> getRoute21(ServerRequest serverRequest) {
        return ServerResponse.ok().body(fromValue("getRoute21"));
    }

    private Mono<ServerResponse> getRoute1(ServerRequest serverRequest) {
        return ServerResponse.ok().body(fromValue("getRoute1"));
    }

    private Mono<ServerResponse> getRoute2(ServerRequest serverRequest) {
        return ServerResponse.ok().body(fromValue("getRoute2"));
    }

}
