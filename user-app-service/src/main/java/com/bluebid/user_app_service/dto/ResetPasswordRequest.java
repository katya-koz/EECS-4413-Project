package com.bluebid.user_app_service.dto;

public class ResetPasswordRequest {
	
	String email;
	String userID;
	String newPassword;
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
