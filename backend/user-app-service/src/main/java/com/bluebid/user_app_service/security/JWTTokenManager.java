package com.bluebid.user_app_service.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.bluebid.user_app_service.dto.TokenResponse;

@Component
public class JWTTokenManager {
	
    private final SecretKey key = Keys.hmacShaKeyFor("6ce0128b814365a2592f3dd44042f83649908c3672db59ef192b56e711e4c649".getBytes()); /// key for testing purposes
	private final long expirationMs = 1000 * 60 * 60 ; // token expires in 60 min - for testing
	
	
	// generate a token with the user's username
	//changed to generate with userid instead
	public TokenResponse generateToken(String userId) {
		Date expiry = new Date(System.currentTimeMillis() + expirationMs);
		return new TokenResponse(Jwts.builder()
        .subject(userId)
        .issuedAt(new Date())
        .expiration(expiry)
        .signWith(key)  
        .compact(), expiry);
	}
	
	//validate a token by checking the signature and expiration
	public boolean validateToken(String token) {
		try {
	        Jwts.parser()
	            .verifyWith(key)
	            .build()
	            .parseSignedClaims(token.replace("Bearer ", ""));
	        return true;
	    } catch (JwtException | IllegalArgumentException e) {
	        return false;
	    }
	}
	// parse username from jwt token
	public String parseUsername(String token) {
		Claims claims = Jwts.parser()
	            .verifyWith(key)
	            .build()
	            .parseSignedClaims(token.replace("Bearer ", ""))
	            .getPayload();
		
	    return claims.getSubject(); // username
	}
	
	

}
