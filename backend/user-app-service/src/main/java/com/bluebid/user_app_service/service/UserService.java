package com.bluebid.user_app_service.service;

import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bluebid.user_app_service.dto.BidInitiatedEvent;
import com.bluebid.user_app_service.dto.CreateUserProfileRequest;
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
	

	
    @KafkaListener(topics= "payment.user-validation-topic", groupId = "user-payment-validation-group", containerFactory = "paymentInitiatedListenerContainerFactory")
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
    
 // since we are using jwt tokens there is not really a need to check if the user exists at every step.
//    @KafkaListener(topics= "bid.user-validation-topic", groupId = "user-bid-validation-group", containerFactory = "bidInitiatedListenerContainerFactory")
//    public void handleCatalogueSuccess(BidInitiatedEvent event) {
//        Optional<User> optionalUser = _userRepository.findById(event.getUserID());
//
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            UserInfoValidationSuccessEvent userInfoEvent = new UserInfoValidationSuccessEvent(
//            	event.getUserID(),
//				event.getId(),
//				user.getFirstName(),
//				user.getLastName(),
//				user.getStreetName(),
//				user.getStreetNum(),
//				user.getCity(),
//				user.getPostalCode(),
//				user.getCountry()
//            );
//
//            _kafkaTemplate.send("user.bid-user-validation-success-topic", userInfoEvent);
//
//        } else {
//            // if user not found, publish a failure
//        	UserInfoValidationFailureEvent failEvent = new UserInfoValidationFailureEvent(
//            		event.getUserID(),
//        			event.getId(),
//                    "User #"+event.getUserID() +" not found in database."
//                    
//            );
//            _kafkaTemplate.send("user.bid-validation-failed-topic", failEvent);
//        }
//    }


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


	public User findOrCreateUserByOAuthIdAndEmail(String oauthUserId, String email, String username) {
	    if (oauthUserId == null || oauthUserId.isEmpty()) {
	        throw new IllegalArgumentException("OAuth user ID cannot be null or empty.");
	    }

	    // check if user with same oauthuserid exists
	    Optional<User> userOpt = _userRepository.findByOauthUserId(oauthUserId);
	    if (userOpt.isPresent()) {
	        return userOpt.get();
	    }

	    // check if user with same email exists
	    if (email != null && !email.isEmpty()) {
	        Optional<User> emailUserOpt = _userRepository.findByEmail(email);
	        if (emailUserOpt.isPresent()) {
	            User emailUser = emailUserOpt.get();
	            emailUser.setOauthUserId(oauthUserId);
	            _userRepository.save(emailUser);
	            return emailUser;
	        }
	    }

	    // create new user
	    User newUser = new User();
	    newUser.setOauthUserId(oauthUserId);
	    newUser.setEmail(email);
	    if(username != null ) newUser.setUsername(username); // may be null but we will take care of that later
	    newUser.setDateRegistered(LocalDateTime.now());

	    // save new user
	    _userRepository.save(newUser);

	    return newUser;
	}


	public void updateUser(CreateUserProfileRequest createProfileRequest, String uid) {
		// fetch existing user 
	    Optional<User> optionalUser = _userRepository.findById(uid);
	    if (optionalUser.isEmpty()) {
	        throw new RuntimeException("User not found");
	    }

	    User user = optionalUser.get();

	    // update only if fields are non null 
	    if (createProfileRequest.getEmail() != null) {
	        user.setEmail(createProfileRequest.getEmail());
	    }
	    if (createProfileRequest.getUsername() != null) {
	        user.setUsername(createProfileRequest.getUsername());
	    }
	    if (createProfileRequest.getPassword() != null) {
	        user.setPassword(createProfileRequest.getPassword()); // consider hashing
	    }
	    if (createProfileRequest.getFirstName() != null) {
	        user.setFirstName(createProfileRequest.getFirstName());
	    }
	    if (createProfileRequest.getLastName() != null) {
	        user.setLastName(createProfileRequest.getLastName());
	    }
	    if (createProfileRequest.getStreetName() != null) {
	        user.setStreetName(createProfileRequest.getStreetName());
	    }
	    if (createProfileRequest.getStreetNum() != null) {
	        user.setStreetNum(createProfileRequest.getStreetNum());
	    }
	    if (createProfileRequest.getCity() != null) {
	        user.setCity(createProfileRequest.getCity());
	    }
	    if (createProfileRequest.getPostalCode() != null) {
	        user.setPostalCode(createProfileRequest.getPostalCode());
	    }
	    if (createProfileRequest.getCountry() != null) {
	        user.setCountry(createProfileRequest.getCountry());
	    }

	    _userRepository.save(user);
	}

	
}
