package com.bluebid.user_app_service.dto;

import java.time.LocalDateTime;

public class ResetPasswordResponse {
	
	private String message;
	private LocalDateTime dateReset;
	
	public ResetPasswordResponse() {
	}
	
	public ResetPasswordResponse(String message, LocalDateTime dateReset) {
	    this.message = message;
	    this.dateReset = dateReset;
	}
	
	public String getMessage() {
	    return message;
	}
	
	public void setMessage(String message) {
	    this.message = message;
	}
	
	public LocalDateTime getDateReset() {
	    return dateReset;
	}
	
	public void setDateReset(LocalDateTime dateReset) {
	    this.dateReset = dateReset;
	}

}
