package com.bluebid.user_app_service.dto;

public class UserInfoFailedEvent  {
	public UserInfoFailedEvent(String failureReason, String paymentId) {
		this.failureReason = failureReason;
		this.paymentId = paymentId;
	}
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	private String failureReason;
	private String paymentId;

}
