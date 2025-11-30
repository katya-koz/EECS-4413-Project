package com.bluebid.user_app_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	            "Login successful.",
	            user.getId()
	        );

	        return ResponseEntity.ok(response);
		}
		
		catch(RuntimeException e) {
			AuthenticationResponse response = new AuthenticationResponse(
		            null,
		            loginRequest.getUsername(),
		            null,
		            e.getMessage(),
		            null
		        );
		
			return ResponseEntity.status(401).body(response);
		}
	}
		
//	@GetMapping("/oauth-callback")
//	public void handleOAuthCallback(
//	        ServerHttpResponse response,
//	        @RequestParam String oauthUserId,
//	        @RequestParam(required = false) String email,
//	        @RequestParam(required = false) String username
//	) {
//	    User user = _userService.findOrCreateUserByOAuthIdAndEmail(oauthUserId, email, username);
//	    //redirect user to fill in shipping information?
//	    
//	    TokenResponse tokenResponse = _jwtManager.generateToken(user.getId());
//	    
//	    if(user.getCountry() != null) {
//	    	// if this account already existed, then the shipping information has been filled out.
//	    	 String redirectUrl = String.format(
//	 	            "http://localhost:3000/oauth-success?token=%s&new=false",
//	 	            tokenResponse.getToken()
//	 	    );
//	    }else { // otherwise, we need to have the user fill in their shipping information on a new account
//	    	String redirectUrl = String.format( 
//	 	            "http://localhost:3000/oauth-success?token=%s&new=true",
//	 	            tokenResponse.getToken()
//	 	    );
//	    }
//	    }

	
	
	
	
}