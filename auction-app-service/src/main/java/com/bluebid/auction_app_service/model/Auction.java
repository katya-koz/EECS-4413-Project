package com.bluebid.auction_app_service.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "auctions")
public class Auction {
	@Id
	private String auctionID; // auction id in mongo (primary key)
	
	private String itemDescription;
	
	private String itemName;
	private String sellerID;
	private String catalogueID;
	private double basePrice;
	private LocalDateTime auctionStartTime;
	private LocalDateTime auctionEndTime;
	private String auctionType = "FORWARD"; // def
	private boolean status;
	private int days;
	private int hours;
	private int duration;
	private String auctionStatus;
	private boolean isValid;
	
	
	
	public Auction(String itemName, String itemDescription, String sellerID, boolean isValid, double basePrice,
			int days, int hours) {
		
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.sellerID = sellerID;
		this.isValid = isValid;
		this.basePrice = basePrice;
		this.days = days;
		this.hours = hours;
	}
	
	public Auction () {
		
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getAuctionStatus() {
		return auctionStatus;
	}

	public void setAuctionStatus(String auctionStatus) {
		this.auctionStatus = auctionStatus;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	

	public String getAuctionID()
	{
		return auctionID;
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
	public String getItemDescription() {
		return itemDescription;
	}


	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}


	public String getItemName() {
		return itemName;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public LocalDateTime calcuateDuration() //this is assuming we have valid input, we should have front end logic preventing invalid input
	{
		
		if (days == 0)
		{
			duration = hours;
		}
		else
		{
			duration = (days * 24) + hours;
		}
		
		return LocalDateTime.now().plusHours(duration);
		
	}
	 
}

