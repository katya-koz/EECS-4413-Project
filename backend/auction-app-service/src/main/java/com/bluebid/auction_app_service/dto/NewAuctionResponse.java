package com.bluebid.auction_app_service.dto;

public class NewAuctionResponse {
	
	private String message;
	private String auctionId;
	private Boolean submitSuccess;
	
	public NewAuctionResponse(String message, String auctionId, Boolean submitSuccess) {
		super();
		this.message = message;
		this.auctionId = auctionId;
		this.submitSuccess = submitSuccess;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAuctionId() {
		return auctionId;
	}
	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}
	public Boolean getSubmitSuccess() {
		return submitSuccess;
	}
	public void setSubmitSuccess(Boolean submitSuccess) {
		this.submitSuccess = submitSuccess;
	}
	
	

}
