package com.bluebid.payment_app_service.controller;

import java.time.LocalDateTime;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
	public ResponseEntity<?> attemptPayment(
	        @RequestBody AttemptPaymentRequest attemptPaymentRequest,
	        @RequestHeader(value = "X-User-Id", required = false) String userId) {

	    if (userId == null || userId.isBlank()) {
	        return ResponseEntity.badRequest().body("Missing user id header.");
	    }

	    String cardNumber = attemptPaymentRequest.getCardNumber();
		String expiryMonth = attemptPaymentRequest.getExpiryMonth();
		String expiryYear = attemptPaymentRequest.getExpiryYear();
		String cvv = attemptPaymentRequest.getSecurityCode();
		Double amount = attemptPaymentRequest.getItemPrice();
		
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

		 String paymentId = _paymentService.isValidPaymentInfo(cardNumber, expiryMonth, expiryYear, cvv, userId, sellerID, catID, time, isExpedited, amount, shippingCost);

	    PaymentResponse response;
	    String base = "http://localhost:8080/api";

	    if (paymentId != null) {
	        response = new PaymentResponse(
	                "The payment was successfully submitted!",
	                attemptPaymentRequest.getItemPrice() + attemptPaymentRequest.getShippingCost(),
	                true,
	                paymentId
	        );
	    } else {
	        response = new PaymentResponse(
	                "The payment was unsuccessful. Credentials invalid.",
	                attemptPaymentRequest.getItemPrice() + attemptPaymentRequest.getShippingCost(),
	                false,
	                null
	        );
	    }

	    EntityModel<PaymentResponse> model = EntityModel.of(response);

	    model.add(Link.of(base + "/payment/payment").withSelfRel());

	    // if succeeded add the receipt link
	    if (paymentId != null) {
	        model.add(Link.of(base + "/payment/receipt/" + paymentId).withRel("receipt"));
	    }

	    // link to catalogue item that was won
	    if (attemptPaymentRequest.getCatalogueID() != null) {
	        model.add(Link.of(base + "/catalogue/items/" + attemptPaymentRequest.getCatalogueID())
	                .withRel("catalogue-item"));
	    }


	    return ResponseEntity.ok(model);
	}
	
	
	@GetMapping("/receipt/{receiptId}")
	public ResponseEntity<?> getReceipt(@PathVariable String receiptId) {
	    Receipt receipt = _paymentService.getReceiptById(receiptId);
	    if (receipt == null) {
	        return ResponseEntity.notFound().build();
	    }

	    String base = "http://localhost:8080/api";

	    EntityModel<Receipt> model = EntityModel.of(receipt);
	    model.add(Link.of(base + "/payment/receipt/" + receiptId).withSelfRel());

	    // link to catalogue item
	    if (receipt.getItemId() != null) {
	        model.add(Link.of(base + "/catalogue/items/" + receipt.getItemId())
	                .withRel("catalogue-item"));
	    }

	    return ResponseEntity.ok(model);
	}


}
