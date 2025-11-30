package com.bluebid.gateway_app_service.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JWTTokenManager {
	
	private final SecretKey key;
	private final long expirationMs;
	 
    public JWTTokenManager(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration-ms}") long expirationMs) {
	    this.key = Keys.hmacShaKeyFor(secret.getBytes());
	    this.expirationMs = expirationMs;
    }
	
	// generate a token with the user's username
	//changed to generate with userid instead
	public String generateToken(String userId) {
		Date expiry = new Date(System.currentTimeMillis() + expirationMs);
		return Jwts.builder()
        .subject(userId)
        .issuedAt(new Date())
        .expiration(expiry)
        .signWith(key)  
        .compact();
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
