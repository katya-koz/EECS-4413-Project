package com.bluebid.auction_app_service.dto;

import java.time.LocalDateTime;
public class NewAuctionRequest {
	
	String sellerID;
	String catalogueID;
	String basePrice;
	LocalDateTime auctionStartTime;
	LocalDateTime auctionEndTime;
	
	public NewAuctionRequest()
	
	{
		
		
	}
	
	public NewAuctionRequest(String sellerID, String catalogueID, String basePrice, LocalDateTime auctionStartTime, LocalDateTime auctionEndTime)
	{
		this.sellerID = sellerID;
		this.catalogueID = catalogueID;
		this.basePrice = basePrice;
		this.auctionEndTime = auctionEndTime;
		this.auctionStartTime = auctionStartTime;
	}
	
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
	public String getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(String basePrice) {
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
//	

}
