package com.bluebid.auction_app_service.dto;

import java.time.LocalDateTime;
public class InitiateAuctionEvent {
	
	private String id;
	private String itemName;
	private String itemDescription;
	private String sellerID;
	private boolean isValid;
	private String catalogueID;
	private double basePrice;
	private int days;
	private int hours;
	private LocalDateTime auctionStartTime;
	private LocalDateTime auctionEndTime;
	
//	public InitiateAuctionEvent(String sellerID, String catalogueID, String basePrice, LocalDateTime auctionStartTime, LocalDateTime auctionEndTime)
//	{
//		this.sellerID = sellerID;
//		this.catalogueID = catalogueID;
//		this.basePrice = basePrice;
//		this.auctionEndTime = auctionEndTime;
//		this.auctionStartTime = auctionStartTime;
//	}
	
	
	public InitiateAuctionEvent(String itemName, String itemDescription, String sellerID, boolean isValid,
			double basePrice, int days, int hours) {
		
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.sellerID = sellerID;
		this.isValid = isValid;
		this.basePrice = basePrice;
		this.days = days;
		this.hours = hours;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getSellerID() {
		return sellerID;
	}

	public void setSellerID(String sellerID) {
		this.sellerID = sellerID;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
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

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

}
