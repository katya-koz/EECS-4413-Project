//package com.bluebid.user_app_service.service;
//import com.bluebid.user_app_service.model.Seller;
//import com.bluebid.user_app_service.model.User;
//import com.bluebid.user_app_service.repository.UserRepository;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTests {
//		
//		@Mock
//	    private UserRepository _userRepository;
//		
//		@Mock
//		private BCryptPasswordEncoder _passwordEncoder;
//	    @InjectMocks
//	    private UserService _userService;
//	    
//	    @Test
//	    public void create_User_With_New_Username_Should_Be_A_Success() {
//	        User user = new User();
//	        user.setUsername("testuser");
//	        user.setPassword("password");
//	        
//	        when(_userRepository.findByUsername("testuser")).thenReturn(Optional.empty()); // user does not already exist
//	        when(_userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // mock save the user
//	        when(_passwordEncoder.encode("password")).thenReturn("hashedPassword");
//	        User created = _userService.createUser(user);
//
//	        assertNotNull(created.getUsername());
//	        assertNotNull(created.getPassword());
//	        assertNotNull(created.getDateRegistered());
//	        
//	        verify(_userRepository).save(user); /// verify that this mock was called
//	        verify(_passwordEncoder).encode("password");
//	    }
//
//	    @Test
//	    public void  username_Not_Taken_Exists() {
//	        User user = new User();
//	        user.setUsername("existingUsername");
//
//	        when(_userRepository.findByUsername("existingUsername")).thenReturn(Optional.of(new User()));
//
//	        RuntimeException ex = assertThrows(RuntimeException.class, () -> _userService.createUser(user));
//	        assertEquals("Username already exists", ex.getMessage());
//	    }
//
//	    @Test
//	    public void create_Seller_With_New_Username_Should_Be_A_Success() {
//	    	Seller user = new Seller();
//			user.setUsername("testuser");
//			user.setPassword("password");
//			
//			when(_userRepository.findByUsername("testuser")).thenReturn(Optional.empty()); // user does not already exist
//			when(_userRepository.save(any(Seller.class))).thenAnswer(invocation -> invocation.getArgument(0)); // mock save the user
//			when(_passwordEncoder.encode("password")).thenReturn("hashedPassword");
//			Seller created = _userService.createSeller(user);
//			
//			assertNotNull(created.getUsername());
//			assertNotNull(created.getPassword());
//			assertNotNull(created.getDateRegistered());
//			
//			verify(_userRepository).save(user); /// verify that this mock was called
//			verify(_passwordEncoder).encode("password");
//	    }
//
//	    @Test
//	    public void successful_Validation_Matches_Passwords() {
//	        User user = new User();
//	        user.setUsername("user1");
//	        user.setPassword("hashedpassword"); 
//	        
//	        when(_userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
//	        when(_passwordEncoder.matches( "password", "hashedpassword")).thenReturn(true); 
//	        
//	        User validated = _userService.validateCredentials("user1", "password");
//	        
//	        assertEquals("user1", validated.getUsername());
//	        
//	        verify(_userRepository).findByUsername("user1");
//	        verify(_passwordEncoder).matches("password", "hashedpassword");
//
////	        User validated = userService.validateCredentials("user1", "password");
////	        assertEquals("user1", validated.getUsername());
//	    }
//
//	    @Test
//	    public void unsuccessful_Validation_Not_Matches_Passwords() {
//	        User user = new User();
//	        user.setUsername("user1");
//	        user.setPassword("password");
//
//	        when(_userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
//
//	        RuntimeException ex = assertThrows(RuntimeException.class, () -> _userService.validateCredentials("user1", "wrongpass"));
//	        assertEquals("Invalid credentials", ex.getMessage());
//	    }
//
//	    @Test
//	    public void unsuccessful_Validation_No_User_Found() {
//	        when(_userRepository.findByUsername("missing")).thenReturn(Optional.empty());
//
//	        RuntimeException ex = assertThrows(RuntimeException.class, () -> _userService.validateCredentials("missing", "pass"));
//	        assertEquals("User not found", ex.getMessage());
//	    }
//
//	    @Test
//	    public void successful_Password_Reset() {
//	        User user = new User();
////	        user.setId("123");
//	        user.setPassword("oldpass");
//
//	        when(_userRepository.findById("123")).thenReturn(Optional.of(user));
//	        when(_userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//	        _userService.resetPassword("123", "newpass");
//
//	        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
//	        verify(_userRepository).save(captor.capture());
//	        assertNotEquals("oldpass", captor.getValue().getPassword());
//	    }
//
//	    @Test
//	    public void unsuccessful_Password_Reset() {
//	        when(_userRepository.findById("999")).thenReturn(Optional.empty());
//
//	        RuntimeException ex = assertThrows(RuntimeException.class, () -> _userService.resetPassword("999", "newpass"));
//	        assertEquals("User not found", ex.getMessage());
//	    }
//	}
//
//
