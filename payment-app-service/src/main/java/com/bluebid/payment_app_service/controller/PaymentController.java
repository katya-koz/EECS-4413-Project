package com.bluebid.payment_app_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluebid.payment_app_service.dto.AttemptPaymentRequest;
import com.bluebid.payment_app_service.service.PaymentService;

@RestController
@RequestMapping("payment")
public class PaymentController {
	
	private final PaymentService _paymentService;
	
	public PaymentController(PaymentService paymentService) {
		this._paymentService= paymentService;
	}
	
	@PostMapping("/payment")
	public ResponseEntity<?> attemptPayment(@RequestBody AttemptPaymentRequest attemptPaymentRequest){
	
		String cardNumber = attemptPaymentRequest.getCardNumber();
		String expiryMonth = attemptPaymentRequest.getExpiryMonth();
		String expiryYear = attemptPaymentRequest.getExpiryYear();
		String cvv = attemptPaymentRequest.getSecurityCode();

		// validate all characters are numeric
		
		if (!cardNumber.matches("\\d+") || !expiryMonth.matches("\\d+") || !expiryYear.matches("\\d+") || !cvv.matches("\\d+")) {
		    return ResponseEntity
		            .badRequest()
		            .body("Payment information contains non numeric characters.");
		}
		
	
		if (_paymentService.isValidPaymentInfo(cardNumber, expiryMonth, expiryYear, cvv)) {
        	// not really validating actual payment info so this is a placeholder
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
		
		
        
	}

}
