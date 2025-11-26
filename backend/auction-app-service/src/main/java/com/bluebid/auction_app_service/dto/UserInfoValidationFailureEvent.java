package com.bluebid.auction_app_service.dto;

public class UserInfoValidationFailureEvent {
    private String userID; 
    private String producerEventID;
    private String message;
   

    public UserInfoValidationFailureEvent() {}


    public UserInfoValidationFailureEvent(String userID, String producerEventID, String message) {
        this.setUserID(userID);
        this.producerEventID = producerEventID;
        this.setMessage(message);
    }


 

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public String getProducerEventID() {
		return producerEventID;
	}


	public void setProducerEventID(String producerEventID) {
		this.producerEventID = producerEventID;
	}


	public String getUserID() {
		return userID;
	}


	public void setUserID(String userID) {
		this.userID = userID;
	}
}
