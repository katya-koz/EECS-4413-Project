package com.bluebid.user_app_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluebid.user_app_service.dto.LoginRequest;


@RestController
@RequestMapping("/account")
public class AuthenticationController {
	
	@PostMapping("/validate-credentials")
	public ResponseEntity<String> validateUserCredentials(@RequestBody LoginRequest loginRequest){
		// validate user
		// create a JWT token to return
		// you need to make a jwt conversion method (given a secret key), convert userid -> jwt token, and one to reverse it from jwt token -> userid
		
		String token = "jwt-token";
		return ResponseEntity.ok(token);
	}
}