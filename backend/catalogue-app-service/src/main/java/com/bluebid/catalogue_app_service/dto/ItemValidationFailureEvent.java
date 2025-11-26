package com.bluebid.catalogue_app_service.dto;

public class ItemValidationFailureEvent {

    private String catalogueId; // item id in catalogue
    private String producerId;
    private String message;
   

    public ItemValidationFailureEvent() {}


    public ItemValidationFailureEvent(String catalogueId, String producerId, String message) {
        this.catalogueId = catalogueId;
        this.producerId = producerId;
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


	public String getProducerId() {
		return producerId;
	}


	public void setProducerId(String producerId) {
		this.producerId = producerId;
	}
}