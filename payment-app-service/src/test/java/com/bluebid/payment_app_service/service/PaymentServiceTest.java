package com.bluebid.payment_app_service.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaymentServiceTest {
	private PaymentService _paymentService;
	
	@BeforeEach
    void setUp() {
        _paymentService = new PaymentService();
    }

	@Test
	void attemptPayment_shouldReturnTrue_whenCardIsValid() {
		String validCardNumber = "4903506535"; // generated from https://www.dcode.fr/luhn-algorithm
		assertTrue(_paymentService.isValidCreditCard(validCardNumber));

	}
	
	@Test
	void attemptPayment_shouldReturnFalse_whenCardIsInvalid() {
		String invalidCardNumber = "3903506535"; 
		assertFalse(_paymentService.isValidCreditCard(invalidCardNumber)); 
	}
	 
	
	@Test
	void attemptPayment_shouldReturnFalse_whenBadInput() {
		String badNumber = "bad"; 
		assertFalse(_paymentService.isValidCreditCard(badNumber)); 
	}
}
