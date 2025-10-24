package com.bluebid.auction_app_service.dto;
// what is sent as a message to subscribers
public class AuctionNotification {
    private String message;
    private String auctionId;
    private double finalPrice;
    private String winnerId;

    public AuctionNotification(String message, String auctionId, double finalPrice, String winnerId) {
        this.message = message;
        this.auctionId = auctionId;
        this.finalPrice = finalPrice;
        this.winnerId = winnerId;
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

	public double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public String getWinnerId() {
		return winnerId;
	}

	public void setWinnerId(String winnerId) {
		this.winnerId = winnerId;
	}

  
}
