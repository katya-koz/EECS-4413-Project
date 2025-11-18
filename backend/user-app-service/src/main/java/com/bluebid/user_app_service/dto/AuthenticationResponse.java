package com.bluebid.user_app_service.dto;


import java.util.Date;

public class AuthenticationResponse {
    private String token;
    private String username;
    private String message;
    private Date expiresAt;

    public AuthenticationResponse(String token, String username, Date expiresAt, String message) {
        this.setToken(token);
        this.setUsername(username);
        this.setMessage(message);
        this.setExpiresAt(expiresAt);
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
