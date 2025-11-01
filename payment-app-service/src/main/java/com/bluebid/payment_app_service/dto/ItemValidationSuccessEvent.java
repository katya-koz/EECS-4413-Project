package com.bluebid.payment_app_service.dto;

public class ItemValidationSuccessEvent {

    private String catalogueID; // item id in catalogue
    private String itemName;
    private Double shippingCost;
    private Double itemCost;
    private int shippingTime; // days to ship
    private String producerEventID;
   

    public ItemValidationSuccessEvent() {}

    public ItemValidationSuccessEvent(String catalogueID, String itemName, int shippingTime, Double itemCost, Double shippingCost, String producerEventID) {
        this.catalogueID = catalogueID;
        this.itemName = itemName;
        this.shippingTime = shippingTime;
        this.shippingCost = shippingCost;
        this.itemCost = itemCost;
        this.setProducerEventID(producerEventID);
        
    }
  
    public String getCatalogueID() { return catalogueID; }
    public void setCatalogueID(String catalogueID) { this.catalogueID = catalogueID; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public Double getShippingCost() { return shippingCost; }
    public void setShippingCost(Double shippingCost) { this.shippingCost = shippingCost; }

    public Double getItemCost() { return itemCost; }
    public void setItemCost(Double itemCost) { this.itemCost = itemCost; }

    public int getShippingTime() { return shippingTime; }
    public void setShippingTime(int shippingTime) { this.shippingTime = shippingTime; }

	public String getProducerEventID() {
		return producerEventID;
	}

	public void setProducerEventID(String producerEventID) {
		this.producerEventID = producerEventID;
	}
}