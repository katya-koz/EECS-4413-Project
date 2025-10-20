package com.bluebid.user_app_service.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class JWTTokenManager {
	
	private final String key = "test-key"; // will implement later with env
	private final long expirationMs = 1000 * 60 * 30 ; // token expires in 30 min
	
	
	// generate a token with the user's username
	public String generateToken(String username) {
		return Jwts.builder().setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS256, key) // sign json with sha256 key to protect integrity of key
                .compact();
	}
	
	//validate a token by checking the signature and expiration
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(key).parseClaimsJws(token);
			return true;
			
		}catch(Exception e) {
			return false;
		}
	}
	// parse username from jwt token
	public String parseUsername(String token) {
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
	
	

}
