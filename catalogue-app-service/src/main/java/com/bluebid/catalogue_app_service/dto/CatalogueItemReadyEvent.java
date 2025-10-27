package com.bluebid.catalogue_app_service.dto;


public class CatalogueItemReadyEvent {
	public CatalogueItemReadyEvent(String catalogueID, String itemName, int shippingTime, Double itemCost, Double shippingCost, String paymentId) {

		this.catalogueID = catalogueID;
		this.itemName = itemName;
		this.shippingTime = shippingTime;
		this.shippingCost = shippingCost;
		this.itemCost = itemCost;
		this.paymentID = paymentId;
	}
	String catalogueID; // item id in catalogue
	String itemName;
	Double shippingCost;
	Double itemCost;
	int shippingTime; // days to ship
	private String paymentID;
}
