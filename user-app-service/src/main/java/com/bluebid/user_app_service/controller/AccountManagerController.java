package com.bluebid.user_app_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bluebid.user_app_service.dto.CreateUserProfileRequest;
import com.bluebid.user_app_service.dto.ResetPasswordRequest;
import com.bluebid.user_app_service.model.User;
import com.bluebid.user_app_service.security.JWTTokenManager;
//import com.bluebid.user_app_service.service.EmailService;
import com.bluebid.user_app_service.service.UserService;

@RestController
@RequestMapping("/account")
public class AccountManagerController {
	
	private final UserService _userService;
//	private final EmailService _emailService;
	
	public AccountManagerController(UserService userService) {
	    this._userService = userService;
//	    this._emailService = emailService;
	}
	
//	@GetMapping("/check-username")
//	public ResponseEntity<Boolean> checkIsUsernameAvailable(@RequestParam String username){
//		// when creating an account check to see that the username has not been taken
//		
//		return ResponseEntity.ok(true);
//	}
//	@GetMapping("/test")
//	public ResponseEntity<String> test(){
//		
//		return ResponseEntity.ok("hello");
//	}
//	
	@GetMapping("/validate")
	public ResponseEntity<Boolean> validateUsername(@RequestParam String username){
		// when attempting to reset an account password, check db to see if the username is valid
		// potentially return the user's security questions here? if that's the route for forgot password. just an idea... some food for thought....
		
		return ResponseEntity.ok(true);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> createUserProfile(@RequestBody CreateUserProfileRequest createProfileRequest){
		// attempt to save new user to db
		 try {
		        User user = new User();
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
		    } catch (RuntimeException e) {
		    	return ResponseEntity
			            .status(HttpStatus.CONFLICT)
			            .body("Username already exits."); // username exists or other error
		    }
		
	}
	
//	@PostMapping("/seller-profile")
//	public ResponseEntity<Boolean> createSellerProfile(@RequestBody CreateSellerProfileRequest createProfileRequest){
//		// attempt to save new user to db
//		try {
//	        Seller user = new Seller();
//	        user.setUsername(createProfileRequest.getUsername());
//	        user.setPassword(createProfileRequest.getPassword());
//	        user.setFirstName(createProfileRequest.getFirstName());
//	        user.setLastName(createProfileRequest.getLastName());
//	        user.setUserType("SELLER");
//	        _userService.createUser(user);
//
//	        return ResponseEntity.ok(true);
//	    } catch (RuntimeException e) {
//	        return ResponseEntity.status(400).body(false); // username exists or other error
//	    }
//	}
	
	@PutMapping("/password")
	public ResponseEntity<Boolean> createUserProfile(@RequestBody ResetPasswordRequest resetPasswordRequest){
		// somehow after validating the user, the user will return the new password they want to set.
		return ResponseEntity.ok(true); // return password reset success or failure
	}
	


}