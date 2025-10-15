package com.bluebid.user_app_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bluebid.user_app_service.dto.CreateSellerProfileRequest;
import com.bluebid.user_app_service.dto.CreateUserProfileRequest;
import com.bluebid.user_app_service.dto.ResetPasswordRequest;

@RestController
@RequestMapping("/account")
public class AccountManagerController {
	
	@GetMapping("/check-username")
	public ResponseEntity<Boolean> checkIsUsernameAvailable(@RequestParam String username){
		// when creating an account check to see that the username has not been taken
		
		return ResponseEntity.ok(true);
	}
	
	@GetMapping("/validate-username")
	public ResponseEntity<Boolean> validateUsername(@RequestParam String username){
		// when attempting to reset an account password, check db to see if the username is valid
		// potentially return the user's security questions here? if that's the route for forgot password. just an idea... some food for thought....
		
		return ResponseEntity.ok(true);
	}
	
	@PostMapping("/create-user-profile")
	public ResponseEntity<Boolean> createUserProfile(@RequestBody CreateUserProfileRequest createProfileRequest){
		// attempt to save new user to db
		return ResponseEntity.ok(true); // return profile creation success or failure
	}
	
	@PostMapping("/create-seller-profile")
	public ResponseEntity<Boolean> createSellerProfile(@RequestBody CreateSellerProfileRequest createProfileRequest){
		// attempt to save new user to db
		return ResponseEntity.ok(true); // return profile creation success or failure
	}
	
	@PostMapping("/reset-passsword")
	public ResponseEntity<Boolean> createUserProfile(@RequestBody ResetPasswordRequest resetPasswordRequest){
		// somehow after validating the user, the user will return the new password they want to set.
		return ResponseEntity.ok(true); // return password reset success or failure
	}
	


}