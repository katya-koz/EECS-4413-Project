package com.bluebid.payment_app_service.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bluebid.payment_app_service.model.Receipt;

public interface PaymentRepository extends MongoRepository<Receipt, String> {

	Optional<Receipt> findByPaymentId(String paymentId);
	
	
}