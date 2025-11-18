package com.bluebid.user_app_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluebid.user_app_service.dto.AuthenticationResponse;
import com.bluebid.user_app_service.dto.LoginRequest;
import com.bluebid.user_app_service.dto.TokenResponse;
import com.bluebid.user_app_service.model.User;
import com.bluebid.user_app_service.security.JWTTokenManager;
import com.bluebid.user_app_service.service.UserService;


@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
	private final UserService _userService;
	private final JWTTokenManager _jwtManager;
	
	public AuthenticationController(UserService userService, JWTTokenManager tokenManager) {
	    this._userService = userService;
	    this._jwtManager = tokenManager;
	}
	
	// public endpoint
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> validateUserCredentials(@RequestBody LoginRequest loginRequest){
		// validate user
		// the passwords are hashed in database
		

		try{
			User user = _userService.validateCredentials(loginRequest.getUsername(), loginRequest.getPassword());

			TokenResponse tokenResponse = _jwtManager.generateToken(user.getId());

	        AuthenticationResponse response = new AuthenticationResponse(
	        	tokenResponse.getToken(),
	            user.getUsername(),
	            tokenResponse.getExpiresAt(),
	            "Login successful."
	        );

	        return ResponseEntity.ok(response);
		}
		
		catch(RuntimeException e) {
			AuthenticationResponse response = new AuthenticationResponse(
		            null,
		            loginRequest.getUsername(),
		            null,
		            e.getMessage()
		        );
		
			return ResponseEntity.status(401).body(response);
		}
	}
}