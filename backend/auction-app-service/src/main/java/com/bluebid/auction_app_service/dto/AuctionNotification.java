package com.bluebid.auction_app_service.dto;

import com.bluebid.auction_app_service.model.Auction;

// what is sent as a message to subscribers
public class AuctionNotification {
    private String message;
    private Auction auction;
    private double finalPrice;
    private String winnerId;

    public AuctionNotification(String message, Auction auction, double finalPrice, String winnerId) {
        this.message = message;
        this.auction = auction;
        this.finalPrice = finalPrice;
        this.winnerId = winnerId;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Auction getAuction() {
		return auction;
	}

	public void setAuction(Auction auction) {
		this.auction = auction;
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
