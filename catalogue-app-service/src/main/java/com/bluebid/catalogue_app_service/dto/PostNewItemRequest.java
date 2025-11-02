package com.bluebid.catalogue_app_service.dto;

import java.time.LocalDateTime;

public class PostNewItemRequest {
	
	String sellerID;
	LocalDateTime postedDate;
	LocalDateTime auctionEndDate;
	String basePrice;
	String itemName;
	String itemDescription;



//	public PostNewItemRequest(String sellerID, LocalDateTime postedDate, LocalDateTime auctionEndDate, String basePrice, String itemName, String itemDescription)
//	{
//		this.sellerID = sellerID;
//		this.postedDate = postedDate;
//		this.auctionEndDate = auctionEndDate;
//		this.basePrice = basePrice;
//		this.itemName = itemName;
//		this.itemDescription = itemDescription;
//	}
	
	public PostNewItemRequest(String sellerID, String basePrice, String itemName, String itemDescription)
	{
		this.sellerID = sellerID;
		this.basePrice = basePrice;
		this.itemName = itemName;
		this.itemDescription = itemDescription;
	}
	
	//getters and setters
	public String getSellerID() {
		return sellerID;
	}

	public void setSellerID(String sellerID) {
		this.sellerID = sellerID;
	}

	public LocalDateTime getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(LocalDateTime postedDate) {
		this.postedDate = postedDate;
	}

	public LocalDateTime getAuctionEndDate() {
		return auctionEndDate;
	}

	public void setAuctionEndDate(LocalDateTime auctionEndDate) {
		this.auctionEndDate = auctionEndDate;
	}

	public String getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(String basePrice) {
		this.basePrice = basePrice;
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

}
