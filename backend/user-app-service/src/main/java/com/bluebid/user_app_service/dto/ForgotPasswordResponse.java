package com.bluebid.user_app_service.dto;

import java.time.LocalDateTime;

public class ForgotPasswordResponse {
	private String message;
	private String token; // token to send to user for password reset. in full implementation it will be emailed.
	private LocalDateTime expiry;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public LocalDateTime getExpiry() {
		return expiry;
	}
	public void setExpiry(LocalDateTime expiry) {
		this.expiry = expiry;
	}
	public ForgotPasswordResponse(String message, String token, LocalDateTime expiry) {
		super();
		this.message = message;
		this.token = token;
		this.expiry = expiry;
	}
	
	
	

}
