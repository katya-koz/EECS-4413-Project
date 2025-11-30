//package com.bluebid.gateway_app_service.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.CorsConfigurationSource;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//
//import java.net.URI;
//import java.util.Arrays;
//
//@Configuration
//public class SecurityConfig {
//
//	@Bean
//	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, JWTTokenManager jwtManager) {
//
//	    http
//	        .csrf(ServerHttpSecurity.CsrfSpec::disable)
//	        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//	        .authorizeExchange(ex -> ex
//	            .pathMatchers("/oauth2/**").permitAll()
//	            .pathMatchers("/api/authentication/oauth-callback").permitAll()
//	            .anyExchange().authenticated()
//	        ).oauth2Login(oauth2 -> oauth2
//	            .authenticationSuccessHandler((webFilterExchange, authentication) -> {
//	                OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
//	                OAuth2User oauthUser = oauthToken.getPrincipal();
//
//	                // get oauth attributes
//	                String oauthUserId = oauthUser.getAttribute("id").toString();
//	                String email = oauthUser.getAttribute("email");
//
//	                // redirect to user service callback on oauth successful authentication
//	                String redirectUrl = String.format(
//	                    "http://localhost:3000/login?oauthUserId=%s&email=%s",
//	                    oauthUserId,
//	                    email
//	                );
//	                
//
//	                webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.FOUND);
//	                webFilterExchange.getExchange().getResponse().getHeaders().setLocation(URI.create(redirectUrl));
//
//	                return webFilterExchange.getExchange().getResponse().setComplete();
//	            })
//	        );
//
//	    return http.build();
//	}
//
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
//        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        config.setAllowedHeaders(Arrays.asList("*"));
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//
//        return source;
//    }
//}
