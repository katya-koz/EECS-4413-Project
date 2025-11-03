package com.bluebid.gateway_app_service.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bluebid.gateway_app_service.security.JwtAuthenticationFilter;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
public class GatewayAppServiceRoute {
	
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder, JwtAuthenticationFilter jwtFilter) {
        return builder.routes()
        		// route requests to the correct service ports 
        		
                .route("account_service_route", r -> r
                        .path("/api/account/**")
                        .filters(f -> f
                        		.filter(jwtFilter) // first apply the jwt filter for authorization
                                .rewritePath("/api/account/(?<segment>.*)", "/account/${segment}") // then change the path to remove 'api'
                                .addRequestHeader("X-Gateway", "BlueBidGateway")) // dont really need this, but maybe for later. ill keep for now
                        .uri("http://user-service:8080")) // redirect to the correct service
                .route("authentication_service_route", r -> r
                        .path("/api/authentication/**")
                        .filters(f -> f
                        		.filter(jwtFilter)
                                .rewritePath("/api/authentication/(?<segment>.*)", "/authentication/${segment}")
                                .addRequestHeader("X-Gateway", "BlueBidGateway"))
                        .uri("http://user-service:8080")) 
                .route("bidding_service_route", r -> r
                        .path("/api/bidding/**")
                        .filters(f -> f
                        		.filter(jwtFilter)
                                .rewritePath("/api/bidding/(?<segment>.*)", "/bidding/${segment}")
                                .addRequestHeader("X-Gateway", "BlueBidGateway"))
                        .uri("http://auction-service:8080"))
                .route("auction_service_route", r -> r
                        .path("/api/auction/**")
                        .filters(f -> f
                        		.filter(jwtFilter)
                                .rewritePath("/api/auction/(?<segment>.*)", "/auction/${segment}")
                                .addRequestHeader("X-Gateway", "BlueBidGateway"))
                        .uri("http://auction-service:8080"))
                .route("catalogue_service_route", r -> r
                        .path("/api/catalogue/**") 
                        .filters(f -> f
                        		.filter(jwtFilter)
                        		//.filters(f -> f.rewritePath("/foo/(?<segment>.*)", "/${segment}"))
                        		.rewritePath("/api/catalogue/(?<segment>.*)", "/catalogue/${segment}")
                                .addRequestHeader("X-Gateway", "BlueBidGateway"))
                        .uri("http://catalogue-service:8080"))
                .route("payment_service_route", r -> r
                        .path("/api/payment/**")
                        .filters(f -> f
                        		.filter(jwtFilter)
                                .rewritePath("/api/payment/(?<segment>.*)", "/payment/${segment}")
                                .addRequestHeader("X-Gateway", "BlueBidGateway"))
                        .uri("http://payment-service:8080"))
                .build();
    }

}

