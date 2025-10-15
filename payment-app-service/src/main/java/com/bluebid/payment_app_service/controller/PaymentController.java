package com.bluebid.payment_app_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluebid.payment_app_service.dto.AttemptPaymentRequest;

@RestController
@RequestMapping("payment")
public class PaymentController {
	
	@PostMapping("/attempt-payment")
	public ResponseEntity<Boolean> attemptPayment(@RequestBody AttemptPaymentRequest attemptPaymentRequest){
	
		
		return ResponseEntity.ok(true);
	}

}
