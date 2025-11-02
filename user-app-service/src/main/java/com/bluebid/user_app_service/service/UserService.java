package com.bluebid.user_app_service.service;

import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bluebid.user_app_service.dto.BidInitiatedEvent;
import com.bluebid.user_app_service.dto.PaymentInitiatedEvent;
import com.bluebid.user_app_service.dto.UserInfoValidationFailureEvent;
import com.bluebid.user_app_service.dto.UserInfoValidationSuccessEvent;
import com.bluebid.user_app_service.model.RecoveryToken;
import com.bluebid.user_app_service.model.User;
import com.bluebid.user_app_service.repository.PasswordRecoveryRepository;
import com.bluebid.user_app_service.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
	private static final long TOKEN_RECOVERY_TIME = 30; // 30 min expiry
	private final UserRepository _userRepository;
	private final BCryptPasswordEncoder _passwordEncoder;
	private final KafkaTemplate<String, Object> _kafkaTemplate;
	private final PasswordRecoveryRepository _tokenRepository;
	
	public UserService(KafkaTemplate<String, Object> kafkaTemplate,UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, PasswordRecoveryRepository tokenRepository) {
	    this._userRepository = userRepository;
	    this._passwordEncoder = passwordEncoder; 
	    this._kafkaTemplate = kafkaTemplate;
	    this._tokenRepository = tokenRepository;
	}
	
	
	public User createUser(User user) {
		_userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new IllegalArgumentException("Username already exists.");
        });
		
		_userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
			throw new IllegalArgumentException("Email can't be used for more than one account.");
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


	public RecoveryToken createRecoveryToken(String email, String username) {
		// see if username and email match
		// if yes, return a recovery token with expiry
		
		Optional<User> userOpt = _userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
			throw new IllegalArgumentException("There is no such user, " + username + " with the email, " + email + ".");
        }
        
        User user = userOpt.get();

        RecoveryToken token = new RecoveryToken(email, username, user.getId(), LocalDateTime.now(), LocalDateTime.now().plusMinutes(TOKEN_RECOVERY_TIME));
        _tokenRepository.save(token);
        
		
		return token;
	}
	
	public String resetPassword(String tokenId, String newPass) {
		
		Optional<RecoveryToken> tokenOpt = _tokenRepository.findById(tokenId);
		if(tokenId.isEmpty() || tokenOpt.isEmpty()) {
			throw new IllegalArgumentException("This token does not exist.");
		}
		
		RecoveryToken token = tokenOpt.get();
		
		if(token.getDateExpired().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("This token is expired. Please generate a new one.");
		}
		
		if(token.getIsUsed()) {
			throw new IllegalArgumentException("This token has already been used to reset a password.");
		}
		
		 User user = _userRepository.findById(token.getUserID()).orElseThrow(() -> new RuntimeException("User not found"));
			// hash the new password
		user.setPassword(_passwordEncoder.encode(newPass));
			// save updated user
		_userRepository.save(user);
		// invalidate recovery token
		token.setIsUsed(true);
		_tokenRepository.save(token);
		
		return "Password has been successfully reset for " +user.getUsername()+". Recovery token is now invalid.";
	}
	
}
