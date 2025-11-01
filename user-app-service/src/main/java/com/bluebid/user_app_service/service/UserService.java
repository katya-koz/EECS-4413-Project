package com.bluebid.user_app_service.service;

import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bluebid.user_app_service.dto.BidInitiatedEvent;
import com.bluebid.user_app_service.dto.PaymentInitiatedEvent;
import com.bluebid.user_app_service.dto.UserInfoValidationFailureEvent;
import com.bluebid.user_app_service.dto.UserInfoValidationSuccessEvent;
import com.bluebid.user_app_service.model.User;
import com.bluebid.user_app_service.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
	// this is the logic layer
	private final UserRepository _userRepository;
	private final BCryptPasswordEncoder _passwordEncoder;
	private final KafkaTemplate<String, Object> _kafkaTemplate;
	
	public UserService(KafkaTemplate<String, Object> kafkaTemplate,UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
	    this._userRepository = userRepository;
	    this._passwordEncoder = passwordEncoder; 
	    this._kafkaTemplate = kafkaTemplate;
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
	
    @KafkaListener(topics= "payment.user-validation-topic", groupId = "user-group", containerFactory = "paymentInitiatedListenerContainerFactory")
	    public void handleCatalogueSuccess(PaymentInitiatedEvent event) {
	        Optional<User> optionalUser = _userRepository.findById(event.getUserID());

	        if (optionalUser.isPresent()) {
	            User user = optionalUser.get();
	            UserInfoValidationSuccessEvent userInfoEvent = new UserInfoValidationSuccessEvent(
	            	event.getUserID(),
					event.getId(),
					user.getFirstName(),
					user.getLastName(),
					user.getStreetName(),
					user.getStreetNum(),
					user.getCity(),
					user.getPostalCode(),
					user.getCountry()
	            );

	            _kafkaTemplate.send("user.payment-user-validation-success-topic", userInfoEvent);

	        } else {
	            // if user not found, publish a failure
	        	UserInfoValidationFailureEvent failEvent = new UserInfoValidationFailureEvent(
	            		event.getUserID(),
	        			event.getId(),
	                    "User #"+event.getUserID() +" not found in database."
	                    
	            );
	            _kafkaTemplate.send("user.payment-validation-failed-topic", failEvent);
	        }
	    }
    
    
    @KafkaListener(topics= "bid.user-validation-topic", groupId = "user-group", containerFactory = "bidInitiatedListenerContainerFactory")
    public void handleCatalogueSuccess(BidInitiatedEvent event) {
        Optional<User> optionalUser = _userRepository.findById(event.getUserID());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserInfoValidationSuccessEvent userInfoEvent = new UserInfoValidationSuccessEvent(
            	event.getUserID(),
				event.getId(),
				user.getFirstName(),
				user.getLastName(),
				user.getStreetName(),
				user.getStreetNum(),
				user.getCity(),
				user.getPostalCode(),
				user.getCountry()
            );

            _kafkaTemplate.send("user.bid-user-validation-success-topic", userInfoEvent);

        } else {
            // if user not found, publish a failure
        	UserInfoValidationFailureEvent failEvent = new UserInfoValidationFailureEvent(
            		event.getUserID(),
        			event.getId(),
                    "User #"+event.getUserID() +" not found in database."
                    
            );
            _kafkaTemplate.send("user.bid-validation-failed-topic", failEvent);
        }
    }
	
}
