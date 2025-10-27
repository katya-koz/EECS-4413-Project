package com.bluebid.catalogue_app_service.dto;

import java.time.LocalDateTime;

import org.apache.kafka.common.Uuid;

public class PaymentInitiatedEvent {
	// publish this event when a payment has been made
	private String id; 
	private String userID; // user making purchase
	private String sellerID; // seller selling item
	private String catalogueID; // item id in catalogue
	private LocalDateTime paymentTime;
	private Boolean isExpedited;
	private Double expectedItemCost; // this is the amount the user expects to pay
	private Double expectedShippingCost;
	
	public PaymentInitiatedEvent(String userID, String sellerID, String catalogueID, LocalDateTime paymentTime, Boolean isExpedited, Double expectedItemCost,  Double expectedShippingCost) {
		this.userID = userID;
		this.sellerID = sellerID;
		this.catalogueID = catalogueID;
		this.paymentTime = paymentTime;
		this.isExpedited = isExpedited;
		this.expectedItemCost = expectedItemCost;
		this.expectedShippingCost = expectedShippingCost;
		this.setId(Uuid.randomUuid().toString());
	}
	
	public Double getExpectedItemCost() {
		return expectedItemCost;
	}
	public void setExpectedItemCost(Double expectedItemCost) {
		this.expectedItemCost = expectedItemCost;
	}
	public Double getExpectedShippingCost() {
		return expectedShippingCost;
	}
	public void setExpectedShippingCost(Double expectedShippingCost) {
		this.expectedShippingCost = expectedShippingCost;
	}
	public Boolean getIsExpedited() {
		return isExpedited;
	}
	public void setIsExpedited(Boolean isExpedited) {
		this.isExpedited = isExpedited;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getSellerID() {
		return sellerID;
	}
	public void setSellerID(String sellerID) {
		this.sellerID = sellerID;
	}
	public String getCatalogueID() {
		return catalogueID;
	}
	public void setCatalogueID(String catalogueID) {
		this.catalogueID = catalogueID;
	}
	public LocalDateTime getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(LocalDateTime paymentTime) {
		this.paymentTime = paymentTime;
	}

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

}
