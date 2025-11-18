package com.bluebid.catalogue_app_service.dto;

import java.time.LocalDateTime;


public class AuctionUploadEvent {

	String catalogueId; //required for uploading to the auction
	String sellerId;
	String basePrice;
	String itemName;
	String itemDescription;
	
	public AuctionUploadEvent()
	{
		
	}
	public AuctionUploadEvent(String catalogueId, String sellerId, String basePrice, String itemName, String itemDescription)
	{
		this.catalogueId = catalogueId;
		this.sellerId = sellerId;
		this.basePrice = basePrice;
		this.itemDescription = itemDescription;
		this.itemName = itemName;
	}

	//getters and setters
	public String getCatalogueId() {
		return catalogueId;
	}

	public void setCatalogueId(String catalogueId) {
		this.catalogueId = catalogueId;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
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
