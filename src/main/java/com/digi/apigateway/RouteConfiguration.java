package com.digi.apigateway;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes().build();
    }


    @Bean
    public RouteLocator restaurantRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/order/**")
                        .filters(f -> f.hystrix(config -> config
                                .setName("order")
                                .setFallbackUri("forward:/fallback/orderFallback")))
                        .uri("lb://RESTAURANT-ORDER"))
                .route(p -> p
                        .path("/kitchen/**")
                        .filters(f -> f.hystrix(config -> config
                                 .setName("kitchen")
                                 .setFallbackUri("forward:/fallback/kitchenFallback")))
                        .uri("lb://RESTAURANT-KITCHEN"))
                .build();
    }

}
