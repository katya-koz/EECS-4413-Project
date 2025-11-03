package com.bluebid.auction_app_service.dto;

public class NewAuctionRequest {
	
	private String itemName;
	private String itemDescription;
	//String sellerID; not needed anymore as the userid sending the request is in the jwt header
	private double basePrice;
	private int seconds;

	
	public NewAuctionRequest() {}
	
	


	public NewAuctionRequest(String itemName, String itemDescription, 
		double basePrice, int seconds) {
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.basePrice = basePrice;
		this.seconds = seconds;
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


	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}



	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}



}
