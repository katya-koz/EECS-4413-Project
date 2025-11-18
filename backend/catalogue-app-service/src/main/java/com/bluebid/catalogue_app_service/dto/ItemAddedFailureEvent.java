package com.bluebid.catalogue_app_service.dto;

public class ItemAddedFailureEvent {

	private String catalogueID;
	private String itemName;
	private String itemDescription;
	private String sellerID;
	private String auctionID;
	private String failureMessage;
	
	public ItemAddedFailureEvent()
	{
		
	}


	public ItemAddedFailureEvent(String catalogueID, String itemName, String itemDescription, String sellerID,
			String auctionID, String failureMessage) 
	{
		
		this.catalogueID = catalogueID;
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.sellerID = sellerID;
		this.auctionID = auctionID;
		this.failureMessage = failureMessage;
	}


	public String getCatalogueID() {
		return catalogueID;
	}


	public void setCatalogueID(String catalogueID) {
		this.catalogueID = catalogueID;
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


	public String getAuctionID() {
		return auctionID;
	}


	public void setAuctionID(String auctionID) {
		this.auctionID = auctionID;
	}


	public String getFailureMessage() {
		return failureMessage;
	}


	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}
}