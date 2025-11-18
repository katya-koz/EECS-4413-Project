package com.bluebid.user_app_service.dto;

import java.util.Date;

public class TokenResponse {
	private String token;
	private Date expiresAt;
	
	
	public TokenResponse(String token, Date expiresAt) {
		this.token = token;
		this.expiresAt = expiresAt;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getExpiresAt() {
		return expiresAt;
	}
	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}
	
	

}
