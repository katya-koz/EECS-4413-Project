package com.bluebid.auction_app_service.dto;

public class BidResponse {
	


	public BidResponse(String catalogueItemId, String userId, Double bidAmount, String auctionId, String bidId,
			String message, Boolean status) {
		
		this.catalogueItemId = catalogueItemId;
		this.userId = userId;
		this.bidAmount = bidAmount;
		this.auctionId = auctionId;
		this.bidId = bidId;
		this.message = message;
		this.setIsSuccess(status);
	}
	
	private String catalogueItemId;
	private String userId;
	private Double bidAmount;
	private String auctionId;
	private String bidId;
	private String message;
	private Boolean isSuccess;
	
	public String getCatalogueItemId() {
		return catalogueItemId;
	}
	public void setCatalogueItemId(String catalogueItemId) {
		this.catalogueItemId = catalogueItemId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Double getBidAmount() {
		return bidAmount;
	}
	public void setBidAmount(Double bidAmount) {
		this.bidAmount = bidAmount;
	}
	public String getAuctionId() {
		return auctionId;
	}
	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}
	public String getBidId() {
		return bidId;
	}
	public void setBidId(String bidId) {
		this.bidId = bidId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Boolean getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	

}
