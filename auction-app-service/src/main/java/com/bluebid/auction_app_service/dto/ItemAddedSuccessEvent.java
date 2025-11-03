package com.bluebid.auction_app_service.dto;

public class ItemAddedSuccessEvent {
	
	private String catalogueID;
	private String itemName;
	private String itemDescription;
	private String sellerID;
	private String auctionID;
	
	public ItemAddedSuccessEvent()
	{
		
	}
	
	public ItemAddedSuccessEvent(String catalogueID, String itemName, String itemDescription, String sellerID, String auctionID)
	{
		this.catalogueID = catalogueID;
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.sellerID = sellerID;
		this.auctionID = auctionID;
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
}
	
	
	
