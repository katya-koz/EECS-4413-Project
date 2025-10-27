package com.bluebid.payment_app_service.dto;

public class ItemReserveFailedEvent {
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPaymentID() {
		return paymentID;
	}
	public void setPaymentID(String paymentID) {
		this.paymentID = paymentID;
	}
	// to keep from hanging, need to publish failed event
	String message;
	String paymentID;
	public ItemReserveFailedEvent(String message, String paymentID) {
		this.message = message;
		this.paymentID = paymentID;
	}
}
