package com.bluebid.gateway_app_service.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
public class GatewayAppServiceRoute {
	
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("account_service_route", r -> r
                        .path("/api/account/**")
                        .filters(f -> f
                                .rewritePath("/api/account/(?<remaining>.*)", "/account/${remaining}")
                                .addRequestHeader("X-Gateway", "BlueBidGateway"))
                        .uri("http://user-service:8080"))
                .route("authentication_service_route", r -> r
                        .path("/api/authentication/**")
                        .filters(f -> f
                                .rewritePath("/api/authentication/(?<remaining>.*)", "/authentication/${remaining}")
                                .addRequestHeader("X-Gateway", "BlueBidGateway"))
                        .uri("http://user-service:8080")) // or your auth-service if separate
                .route("bidding_service_route", r -> r
                        .path("/api/bidding/**")
                        .filters(f -> f
                                .rewritePath("/api/bidding/(?<remaining>.*)", "/bidding/${remaining}")
                                .addRequestHeader("X-Gateway", "BlueBidGateway"))
                        .uri("http://payment-service:8080")) // replace with bidding-service
                .route("auction_service_route", r -> r
                        .path("/api/auction/**")
                        .filters(f -> f
                                .rewritePath("/api/auction/(?<remaining>.*)", "/auction/${remaining}")
                                .addRequestHeader("X-Gateway", "BlueBidGateway"))
                        .uri("http://auction-service:8080"))
                .route("catalogue_service_route", r -> r
                        .path("/api/catalogue/**")
                        .filters(f -> f
                                .rewritePath("/api/catalogue/(?<remaining>.*)", "/catalogue/${remaining}")
                                .addRequestHeader("X-Gateway", "BlueBidGateway"))
                        .uri("http://catalogue-service:8080"))
                .route("payment_service_route", r -> r
                        .path("/api/payment/**")
                        .filters(f -> f
                                .rewritePath("/api/payment/(?<remaining>.*)", "/payment/${remaining}")
                                .addRequestHeader("X-Gateway", "BlueBidGateway"))
                        .uri("http://payment-service:8080"))
                .build();
    }

}

