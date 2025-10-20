package com.bluebid.payment_app_service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PaymentAppServiceApplicationTests 
{

	@Test
	void contextLoads() {
	}
	
	@Test
	void validateCard()
	{
		String cardNumber = "4506445393645302";
		int expiryMonth = 01;
		int expiryYear = 2026;
		String cvv = "123";
		
		PaymentValidation validate = new PaymentValidation(cardNumber, expiryMonth, expiryYear, cvv);
		
		assertEquals(true, validate.validateCard());
		
	}
	
	@Test
	void checkWrongExpiryMonth()
	{
		String cardNumber = "4506445393645302";
		int expiryMonth = 13;
		int expiryYear = 2026;
		String cvv = "123";
		
		PaymentValidation validate = new PaymentValidation(cardNumber, expiryMonth, expiryYear, cvv);
		
		assertEquals(false, validate.validateCard());
		
	}
	@Test
	void checkWrongExpiryYear()
	{
		String cardNumber = "4506445393645302";
		int expiryMonth = 01;
		int expiryYear = 2025;
		String cvv = "123";
		
		PaymentValidation validate = new PaymentValidation(cardNumber, expiryMonth, expiryYear, cvv);
		
		assertEquals(false, validate.validateCard());
		
	}

	@Test
	void checkWrongCVV()
	{
		String cardNumber = "4506445393645302";
		int expiryMonth = 01;
		int expiryYear = 2025;
		String cvv = "45678";
		
		PaymentValidation validate = new PaymentValidation(cardNumber, expiryMonth, expiryYear, cvv);
		
		assertEquals(false, validate.validateCard());
		
	}

}
