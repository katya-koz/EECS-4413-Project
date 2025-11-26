//package com.bluebid.payment_app_service.service;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.time.LocalDate;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//public class PaymentServiceTest {
//	private PaymentService _paymentService;
//	
//	@BeforeEach
//    void setUp() {
//        _paymentService = new PaymentService();
//    }
//
//	@Test
//	void attempting_Payment_should_Return_True_If_Card_Is_Valid() {
//		String validCardNumber = "4903506535"; // generated from https://www.dcode.fr/luhn-algorithm
//		String validExpiryMonth = "1";
//		String validExpiryYear = LocalDate.now().plusYears(2).getYear() + "";
//		String validCVV = "123";
//		assertTrue(_paymentService.isValidCard(validCardNumber, validExpiryMonth, validExpiryYear,validCVV));
//
//	}
//	
//	@Test
//	void attempting_Payment_should_Return_False_If_Card_Is_Invalid() {
//		String invalidCardNumber = "3903506535"; 
//		String invalidExpiryMonth = "1";
//		String invalidExpiryYear = LocalDate.now().plusYears(2).getYear() + "";
//		String invalidCVV = "123";
//		assertFalse(_paymentService.isValidCard(invalidCardNumber, invalidExpiryMonth, invalidExpiryYear,invalidCVV)); 
//	}
//	 
//	
//	@Test
//	void attempting_Payment_should_Return_False_If_Bad_Input() {
//		String badNumber = "bad"; 
//		assertFalse(_paymentService.isValidCard(badNumber, "1", "2025", "123")); 
//	}
//	
//	
//	@Test
//	void validateCard()
//	{
//		String cardNumber = "4506445393645302";
//		String expiryMonth = "01";
//		String expiryYear = "2026";
//		String cvv = "123";
//		
//		assertTrue( _paymentService.isValidCard(cardNumber, expiryMonth, expiryYear, cvv));
//		
//	}
//	
//	@Test
//	void checkWrongExpiryMonth()
//	{
//		String cardNumber = "4506445393645302";
//		String expiryMonth = "13";
//		String expiryYear = "2026";
//		String cvv = "123";
//		
//		assertFalse(_paymentService.isValidCard(cardNumber, expiryMonth, expiryYear, cvv));		
//	}
//	@Test
//	void checkWrongExpiryYear()
//	{
//		String cardNumber = "4506445393645302";
//		String expiryMonth = "01";
//		String expiryYear = "2024";
//		String cvv = "123";
//		
//		
//		
//		assertFalse(_paymentService.isValidCard(cardNumber, expiryMonth, expiryYear, cvv));
//		
//	}
//
//	@Test
//	void checkWrongCVV()
//	{
//		String cardNumber = "4506445393645302";
//		String expiryMonth = "01";
//		String expiryYear = "2025";
//		String cvv = "45678";
//		
//		assertFalse(_paymentService.isValidCard(cardNumber, expiryMonth, expiryYear, cvv));
//		
//	}
//}
