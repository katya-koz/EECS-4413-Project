package com.bluebid.user_app_service.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

@Component
public class JWTTokenManager {
	
    private final SecretKey key = Keys.hmacShaKeyFor("6ce0128b814365a2592f3dd44042f83649908c3672db59ef192b56e711e4c649".getBytes()); /// key for testing purposes
	private final long expirationMs = 1000 * 60 * 30 ; // token expires in 30 min
	
	
	// generate a token with the user's username
	public String generateToken(String username) {
		return Jwts.builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expirationMs))
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
