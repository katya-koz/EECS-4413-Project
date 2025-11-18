package com.bluebid.user_app_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "tokens")
public class RecoveryToken {

    @Id
    private String id; // db id (primary key)
    private String email;
    private String username;
    private String userID;
    private LocalDateTime dateRequested;
	private LocalDateTime dateExpired;
	private Boolean isUsed;
	
	
	
	public RecoveryToken(String email, String username, String userID, LocalDateTime dateRequested, LocalDateTime dateExpired) {
		this.email = email;
		this.username = username;
		this.userID = userID;
		this.dateRequested = dateRequested;
		this.dateExpired = dateExpired;
		
		this.isUsed = false;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public LocalDateTime getDateRequested() {
		return dateRequested;
	}
	public void setDateRequested(LocalDateTime dateRequested) {
		this.dateRequested = dateRequested;
	}
	public LocalDateTime getDateExpired() {
		return dateExpired;
	}
	public void setDateExpired(LocalDateTime dateExpired) {
		this.dateExpired = dateExpired;
	}
	public Boolean getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	

}