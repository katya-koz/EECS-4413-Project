package com.bluebid.user_app_service.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluebid.user_app_service.dto.CreateUserProfileRequest;
import com.bluebid.user_app_service.dto.ForgotPasswordRequest;
import com.bluebid.user_app_service.dto.ForgotPasswordResponse;
import com.bluebid.user_app_service.dto.PasswordResetRequest;
import com.bluebid.user_app_service.dto.ResetPasswordRequest;
import com.bluebid.user_app_service.dto.ResetPasswordResponse;
import com.bluebid.user_app_service.model.RecoveryToken;
import com.bluebid.user_app_service.model.User;
//import com.bluebid.user_app_service.service.EmailService;
import com.bluebid.user_app_service.service.UserService;

@RestController
@RequestMapping("/account")
public class AccountManagerController {
	
	private final UserService _userService;
//	private final EmailService _emailService;
	
	public AccountManagerController(UserService userService) {
	    this._userService = userService;
//	    this._emailService = emailService; to be implemented!
	}
	
	
	@PostMapping("/forgot-password")
	public ResponseEntity<ForgotPasswordResponse> forgotPassword(@RequestBody ForgotPasswordRequest req){
		// initial request where a user sends an email and username to request a code to change their password
		 try {
			 
			RecoveryToken token =  _userService.createRecoveryToken(req.getEmail(), req.getUsername());
			 
			return ResponseEntity.ok(new ForgotPasswordResponse("An email with a recovery token has been sent to " + req.getEmail()  +
					". For testing purposes, I am returning that recovery token link to you, in this response here: " + 
					"http://localhost:8080/api/account/reset-password/" + token.getId(),
					token.getId(), token.getDateExpired() ));
			
		 }catch(Exception e) {
			 return ResponseEntity
		                .status(HttpStatus.BAD_REQUEST)
		                .body(new ForgotPasswordResponse(e.getMessage(), null, null));
		 }
		
		
	}
	
	@PutMapping("/reset-password/{tokenId}")
	public ResponseEntity<ResetPasswordResponse> resetUserPassword(@RequestBody ResetPasswordRequest resetPasswordRequest, @PathVariable String tokenId) {

	    try {
	        _userService.resetPassword(tokenId, resetPasswordRequest.getNewPassword());
	        return ResponseEntity.ok(new ResetPasswordResponse( "Password reset successful", LocalDateTime.now()
	        ));
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(new ResetPasswordResponse(e.getMessage(), null));
	    }
	}

	
	@PostMapping("/signup")
	public ResponseEntity<?> createUserProfile(@RequestBody CreateUserProfileRequest createProfileRequest){
		// attempt to save new user to db
		 try {
		        User user = new User();
		        user.setEmail(createProfileRequest.getEmail());
		        user.setUsername(createProfileRequest.getUsername());
		        user.setPassword(createProfileRequest.getPassword());
		        user.setFirstName(createProfileRequest.getFirstName());
		        user.setLastName(createProfileRequest.getLastName());
		        user.setStreetName(createProfileRequest.getStreetName());
		        user.setStreetNum(createProfileRequest.getStreetNum());
		        user.setCity(createProfileRequest.getCity());
		        user.setPostalCode(createProfileRequest.getPostalCode());
		        user.setCountry(createProfileRequest.getCountry());

		        _userService.createUser(user);

		        return ResponseEntity.ok("New user, "+ user.getUsername() +" successfully created!");
		    } catch (IllegalArgumentException e) {
		        return ResponseEntity
		                .status(HttpStatus.BAD_REQUEST)
		                .body(e.getMessage());
		    }
		
	}
	

	

	


}