package com.bluebid.payment_app_service.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluebid.payment_app_service.dto.AttemptPaymentRequest;
import com.bluebid.payment_app_service.dto.PaymentResponse;
import com.bluebid.payment_app_service.model.Receipt;
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
		Double amount = attemptPaymentRequest.getItemPrice();
		
		String userID = attemptPaymentRequest.getUserID();
		String sellerID = attemptPaymentRequest.getSellerID();
		String catID = attemptPaymentRequest.getCatalogueID();
		Boolean isExpedited = attemptPaymentRequest.getIsExpedited();
		Double shippingCost = attemptPaymentRequest.getShippingCost();
		LocalDateTime time = LocalDateTime.now();

		// validate all characters are numeric
		if (!cardNumber.matches("\\d+") || !expiryMonth.matches("\\d+") || !expiryYear.matches("\\d+") || !cvv.matches("\\d+")) {
		    return ResponseEntity
		            .badRequest()
		            .body("Payment information contains non numeric characters.");
		}
		
		//validate that the amount is not negative or too humongous 
	  
        if(amount < 0 || amount > Double.MAX_VALUE || shippingCost < 0 || shippingCost > Double.MAX_VALUE ) {
        	return ResponseEntity
		            .badRequest()
		            .body("Payment and/or shipping amount is not valid.");
        }
	   
        String paymentId = _paymentService.isValidPaymentInfo(cardNumber, expiryMonth, expiryYear, cvv, userID, sellerID, catID, time, isExpedited, amount, shippingCost);
        
        if (paymentId != null) {
            return ResponseEntity.ok(new PaymentResponse(
                "The payment was successfully submitted!",
                amount + shippingCost,
                true,
                paymentId 
            ));
        } else {
            return ResponseEntity.ok(new PaymentResponse(
                "The payment was unsuccessful. Credentials invalid.",
                amount + shippingCost,
                false,
                null
            ));
        }
		
		
        
	}
	
	
	@GetMapping("/receipt/{paymentId}")
	public ResponseEntity<?> getReceipt(@PathVariable String paymentId) {
	    Receipt receipt = _paymentService.getReceiptByPaymentId(paymentId);
	    if (receipt != null) {
	        return ResponseEntity.ok(receipt);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

}
