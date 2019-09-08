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


    /*@Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/api/v1/first/**")
                        .filters(f -> f.rewritePath("/api/v1/first/(?.*)", "/${remains}")
                                .addRequestHeader("X-first-Header", "first-service-header")
                                .hystrix(c -> c.setName("hystrix")
                                        .setFallbackUri("forward:/fallback/first")))
                        .uri("lb://FIRST-SERVICE/")
                        .id("first-service"))

                .route(r -> r.path("/api/v1/second/**")
                        .filters(f -> f.rewritePath("/api/v1/second/(?.*)", "/${remains}")
                                .hystrix(c -> c.setName("hystrix")
                                        .setFallbackUri("forward:/fallback/second")))
                        .uri("lb://SECOND-SERVICE/")
                        .id("second-service"))
                .build();
    }*/
}
