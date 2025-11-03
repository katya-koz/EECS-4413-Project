package com.bluebid.catalogue_app_service.dto;

public class ItemValidationFailureEvent {

    private String catalogueId; // item id in catalogue
    private String auctionId;
    private String message;
   

    public ItemValidationFailureEvent() {}


    public ItemValidationFailureEvent(String catalogueId, String auctionId, String message) {
        this.catalogueId = catalogueId;
        this.auctionId = auctionId;
        this.setMessage(message);
    }


    public String getCatalogueId() {
    	return catalogueId; 
	}
    
    public void setCatalogueId(String catalogueId) {
    	this.catalogueId = catalogueId; 
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public String getAuctionId() {
		return auctionId;
	}


	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}
}