package com.bluebid.payment_app_service.dto;

public class CatalogueItemReadyEvent {

    private String catalogueID; // item id in catalogue
    private String itemName;
    private Double shippingCost;
    private Double itemCost;
    private int shippingTime; // days to ship
    private String paymentID;

    public CatalogueItemReadyEvent() {}

    public CatalogueItemReadyEvent(String catalogueID, String itemName, int shippingTime, Double itemCost, Double shippingCost, String paymentID) {
        this.catalogueID = catalogueID;
        this.itemName = itemName;
        this.shippingTime = shippingTime;
        this.shippingCost = shippingCost;
        this.itemCost = itemCost;
        this.paymentID = paymentID;
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

    public String getPaymentID() { return paymentID; }
    public void setPaymentID(String paymentID) { this.paymentID = paymentID; }
}