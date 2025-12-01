package com.bluebid.user_app_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.HttpStatus;

import com.bluebid.user_app_service.dto.TokenResponse;
import com.bluebid.user_app_service.model.User;
import com.bluebid.user_app_service.security.JWTTokenManager;
import com.bluebid.user_app_service.service.UserService;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
	
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, JWTTokenManager jwtManager, UserService userService) {

	    http
	        .csrf(ServerHttpSecurity.CsrfSpec::disable) // disable cors and adding extra headers. gateway handles that.
	        .csrf(csrf -> csrf.disable()) 
            .cors(cors -> cors.disable())    
            .headers(headers -> headers.disable()) 
	        //.cors(cors -> cors.configurationSource(corsConfigurationSource()))
	        .authorizeExchange(ex -> ex
	            .pathMatchers("/oauth2/**").permitAll()
	            .pathMatchers("/authentication/oauth-callback").permitAll()
	            .anyExchange().permitAll() // gateway secures our app. user service only makes jwt and handles login.
	        )
	        .oauth2Login(Customizer.withDefaults())
	        .oauth2Client(Customizer.withDefaults())
	        .oauth2Login(oauth -> oauth
	        		
	        		.authenticationSuccessHandler((webFilterExchange, authentication) -> {

	                    OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

	                    String oauthUserId = oauthUser.getAttribute("id").toString();
	                    String email = oauthUser.getAttribute("email");
	                    String username = oauthUser.getAttribute("login");

	                    User user = userService.findOrCreateUserByOAuthIdAndEmail(oauthUserId, email, username);
	                    boolean hasShippingInfo = true;
	                    boolean hasName = true;
	                    
	                    if(user.getCity() == null || user.getCountry() == null 
	                    		|| user.getStreetName() == null || user.getStreetNum() == null || user.getPostalCode() == null) {
	                    	hasShippingInfo = false;
	                    
	                    }
	                    if(user.getFirstName() == null || user.getLastName() == null) {
	                    	hasName = false;
	                    }
	                   
	                    TokenResponse token = jwtManager.generateToken(user.getId());

	                    String redirectUrl = String.format(
	                    	    "http://localhost:3000/oauth-success?uid=%s&uname=%s&email=%s&jwt=%s&expires=%s&hasname=%s&hasshipinfo=%s", // on success, redirect here for the front end
	                    	    URLEncoder.encode(user.getId(), StandardCharsets.UTF_8),
	                    	    URLEncoder.encode(user.getUsername(), StandardCharsets.UTF_8),
	                    	    URLEncoder.encode(email == null ? "" : email, StandardCharsets.UTF_8), // email may be null (with github)
	                    	    URLEncoder.encode(token.getToken(), StandardCharsets.UTF_8),
	                    	    URLEncoder.encode(token.getExpiresAt().toString(), StandardCharsets.UTF_8),
	                    	    hasName,
	                    	    hasShippingInfo
	                    	);

	                    webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.FOUND);
		                webFilterExchange.getExchange().getResponse().getHeaders().setLocation(URI.create(redirectUrl));
	
		                return webFilterExchange.getExchange().getResponse().setComplete();})
	            );

	    return http.build();
	}



}


