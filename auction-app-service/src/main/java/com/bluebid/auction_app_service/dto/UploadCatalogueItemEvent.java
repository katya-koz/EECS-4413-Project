package com.bluebid.auction_app_service.dto;

import java.time.LocalDateTime;

public class UploadCatalogueItemEvent {
	private String sellerID;
    private String itemName;
    private LocalDateTime auctionStartDate;
    private LocalDateTime auctionEndDate;  
    private String itemDescription;
    private double basePrice;
	private String auctionId;
    
    UploadCatalogueItemEvent(){
    	
    	
    }
    

	public UploadCatalogueItemEvent(String sellerID, String itemName,
			 LocalDateTime auctionStartDate, LocalDateTime auctionEndDate, String itemDescription, double price, String aid) {
	
		this.sellerID = sellerID;
		this.itemName = itemName;
		this.auctionId = aid;
		this.auctionStartDate = auctionStartDate;
		this.auctionEndDate = auctionEndDate;
		this.setBasePrice(price);
		this.itemDescription = itemDescription;

	}



	public String getSellerID() {
		return sellerID;
	}

	public void setSellerID(String sellerID) {
		this.sellerID = sellerID;
	}


	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	public LocalDateTime getAuctionStartDate() {
		return auctionStartDate;
	}

	public void setAuctionStartDate(LocalDateTime auctionStartDate) {
		this.auctionStartDate = auctionStartDate;
	}

	public LocalDateTime getAuctionEndDate() {
		return auctionEndDate;
	}

	public void setAuctionEndDate(LocalDateTime auctionEndDate) {
		this.auctionEndDate = auctionEndDate;
	}

	

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}


	public double getBasePrice() {
		return basePrice;
	}


	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}


	public String getAuctionId() {
		return auctionId;
	}


	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}


	
    
    
}
