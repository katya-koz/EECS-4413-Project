package com.bluebid.auction_app_service.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "auctions")
public class Auction {
	@Id
	private String id; // auction id in mongo (primary key)
	private String sellerID;
	private String catalogueID;
	private double basePrice;
	private LocalDateTime auctionStartTime;
	private LocalDateTime auctionEndTime;
	private String auctionType = "FORWARD"; // def
	private boolean status;

	public String getSellerID() {
		return sellerID;
	}
	public void setSellerID(String sellerID) {
		this.sellerID = sellerID;
	}
	public String getCatalogueID() {
		return catalogueID;
	}
	public void setCatalogueID(String catalogueID) {
		this.catalogueID = catalogueID;
	}
	public double getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}
	public LocalDateTime getAuctionStartTime() {
		return auctionStartTime;
	}
	public void setAuctionStartTime(LocalDateTime auctionStartTime) {
		this.auctionStartTime = auctionStartTime;
	}
	public LocalDateTime getAuctionEndTime() {
		return auctionEndTime;
	}
	public void setAuctionEndTime(LocalDateTime auctionEndTime) {
		this.auctionEndTime = auctionEndTime;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getAuctionType() {
		return auctionType;
	}
	public void setAuctionType(String auctionType) {
		this.auctionType = auctionType;
	}

 
}

