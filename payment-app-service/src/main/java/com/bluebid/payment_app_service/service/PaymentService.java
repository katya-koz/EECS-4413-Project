package com.bluebid.payment_app_service.service;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {
	
	 public boolean isValidCreditCard(String cardNumber) {
		 // luhns algo
	        if (cardNumber == null || !cardNumber.matches("\\d+")) { // card should be digits only
	            return false; 
	        }

	        int sum = 0;
	        boolean alternate = false;
	        for (int i = cardNumber.length() - 1; i >= 0; i--) {
	            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
	            if (alternate) {
	                n *= 2;
	                if (n > 9) {
	                    n -= 9;
	                }
	            }
	            sum += n;
	            alternate = !alternate;
	        }
	        return (sum % 10 == 0);
	    }
}
