package com.bluebid.user_app_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluebid.user_app_service.dto.LoginRequest;
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
	
	@PostMapping("/validate-credentials")
	public ResponseEntity<String> validateUserCredentials(@RequestBody LoginRequest loginRequest){
		// validate user
		// the passwords are hashed in database

		try{
			User user = _userService.validateCredentials(loginRequest.getUsername(), loginRequest.getPassword());

			String token = _jwtManager.generateToken(user.getUsername());
			return ResponseEntity.ok(token);}
		
		catch(RuntimeException e) {
			return ResponseEntity.status(401).body(e.getMessage());
		}
	}
}