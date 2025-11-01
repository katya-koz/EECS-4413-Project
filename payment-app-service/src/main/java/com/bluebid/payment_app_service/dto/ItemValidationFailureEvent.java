package com.bluebid.payment_app_service.dto;

public class ItemValidationFailureEvent {

    private String catalogueID; // item id in catalogue
    private String producerEventID;
    private String message;
   

    public ItemValidationFailureEvent() {}


    public ItemValidationFailureEvent(String catalogueID, String producerEventID, String message) {
        this.catalogueID = catalogueID;
        this.producerEventID = producerEventID;
        this.setMessage(message);
    }


    public String getCatalogueID() {
    	return catalogueID; 
	}
    
    public void setCatalogueID(String catalogueID) {
    	this.catalogueID = catalogueID; 
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public String getProducerEventID() {
		return producerEventID;
	}


	public void setProducerEventID(String producerEventID) {
		this.producerEventID = producerEventID;
	}
}