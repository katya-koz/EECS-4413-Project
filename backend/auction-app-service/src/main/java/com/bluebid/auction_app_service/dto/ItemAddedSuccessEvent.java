package com.bluebid.auction_app_service.dto;

public class ItemAddedSuccessEvent {
	
	private String catalogueID;
	private String itemName;
	private String itemDescription;
	private String sellerId;
	private String auctionId;
	
	public ItemAddedSuccessEvent()
	{
		
	}
	
	public ItemAddedSuccessEvent(String catalogueID, String itemName, String itemDescription, String sellerId, String auctionId)
	{
		this.catalogueID = catalogueID;
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.sellerId = sellerId;
		this.auctionId = auctionId;
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

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerID(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getAuctionID() {
		return auctionId;
	}

	public void setAuctionID(String auctionId) {
		this.auctionId = auctionId;
	}
}
	
	
	
