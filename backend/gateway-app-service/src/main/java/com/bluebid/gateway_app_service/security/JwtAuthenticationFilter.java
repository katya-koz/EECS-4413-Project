package com.bluebid.gateway_app_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component // to inject
public class JwtAuthenticationFilter  implements GatewayFilter {

    private final SecretKey key = Keys.hmacShaKeyFor("6ce0128b814365a2592f3dd44042f83649908c3672db59ef192b56e711e4c649".getBytes()); // temp - use env. this is the same as in user service's jwt 
    
    private static final List<String> PUBLIC_ENDPOINTS = List.of( // this may not be the best way to do this, but we need certain endpoints to be exposed publicly without authorization (for example, forgot password, log in, etc).
    	    "/api/authentication/login",
    	    "/api/account/signup",
    	    "/api/account/forgot-password",
    	    "/api/account/reset-password"
    	);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    	String path = exchange.getRequest().getPath().toString();

        if (PUBLIC_ENDPOINTS.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange); // skip jwt check for public endpoints
        }
    	
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7); // remove "Bearer " from header

        try {
        	// verify that this jwt token was given to the user with the secret key
            Claims claims = Jwts.parser()
            		.verifyWith(key)
            		.build()
            		.parseSignedClaims(token.replace("Bearer ", ""))
            		.getPayload();

            // now pass the deconstructed userid with every request
            
            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-User-Id", claims.getSubject())
                    .build();
            
            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(mutatedRequest)
                    .build();
            
//            exchange.getRequest()
//                    .mutate()
//                    .header("X-User-Id", claims.getSubject())
//                    .build();

            return chain.filter(mutatedExchange);
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}




