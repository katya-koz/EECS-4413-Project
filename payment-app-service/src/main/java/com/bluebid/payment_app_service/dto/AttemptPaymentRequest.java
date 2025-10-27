package com.bluebid.payment_app_service.dto;

import java.time.LocalDateTime;

public class AttemptPaymentRequest {
	//payment details
	String cardNumber;
	String securityCode;
	String expiryMonth;
	String expiryYear;
	Double itemPrice;
	Double shippingCost;
	
	
	// ids for cat item, user and seller
	String userID; // user making purchase
	String sellerID; // seller selling item
	String catalogueID; // item id in catalogue
	LocalDateTime paymentTime;
	Boolean isExpedited;
	
	
	public Double getShippingCost() {
		return shippingCost;
	}
	public void setShippingCost(Double shippingCost) {
		this.shippingCost = shippingCost;
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
	public String getExpiryMonth() {
		return expiryMonth;
	}
	public void setExpiryMonth(String expiryMonth) {
		this.expiryMonth = expiryMonth;
	}
	public String getExpiryYear() {
		return expiryYear;
	}
	public void setExpiryYear(String expiryYear) {
		this.expiryYear = expiryYear;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public Double getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Double price) {
		this.itemPrice = price;
	}


}
