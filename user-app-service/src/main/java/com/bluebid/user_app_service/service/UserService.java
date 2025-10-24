package com.bluebid.user_app_service.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bluebid.user_app_service.model.Seller;
import com.bluebid.user_app_service.model.User;
import com.bluebid.user_app_service.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class UserService {
	// this is the logic layer
	private final UserRepository _userRepository;
	private final BCryptPasswordEncoder _passwordEncoder;
	
	public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
	    this._userRepository = userRepository;
	    this._passwordEncoder = passwordEncoder; 
	}
	
	
	public User createUser(User user) {
		_userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new RuntimeException("Username already exists");
        });

        // hash password
        user.setPassword(_passwordEncoder.encode(user.getPassword()));
        // set registration date
        user.setDateRegistered(LocalDateTime.now());

        // save user
        return _userRepository.save(user);
	}
	
	public Seller createSeller(Seller user) {
		_userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new RuntimeException("Username already exists");
        });

        // hash password if needed
        user.setPassword(_passwordEncoder.encode(user.getPassword()));

        // set registration date
        user.setDateRegistered(LocalDateTime.now());

        // save user
        return _userRepository.save(user);
	}
	
	
	public User validateCredentials(String username, String password) {
		
		User user = _userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

		if (!_passwordEncoder.matches(password, user.getPassword())) {
		    throw new RuntimeException("Invalid credentials");
		}
		
		return user; // valid user

	}
	
	public void resetPassword(String userId, String newPass) {
		
		// this isnt entirely secure. need to send a code via email
		
		 User user = _userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		// hash the new password
		user.setPassword(_passwordEncoder.encode(newPass));
		// save updated user
		_userRepository.save(user);
	}
}
