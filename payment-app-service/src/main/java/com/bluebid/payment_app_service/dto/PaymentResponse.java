package com.bluebid.payment_app_service.dto;

public class PaymentResponse {
	
	private String paymentId;
	public PaymentResponse(String message, Double paymentAmount, Boolean paymentSuccess, String paymentId) {
	
		this.message = message;
		this.paymentAmount = paymentAmount;
		this.paymentSubmitSuccess = paymentSuccess;
		this.setPaymentId(paymentId);
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Double getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public Boolean getPaymentSuccess() {
		return paymentSubmitSuccess;
	}
	public void setPaymentSuccess(Boolean paymentSuccess) {
		this.paymentSubmitSuccess = paymentSuccess;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	String message;
	Double paymentAmount;
	Boolean paymentSubmitSuccess;

}
