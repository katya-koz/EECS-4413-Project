package com.bluebid.payment_app_service.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "receipts")
public class Receipt {
	public Receipt(String paymentId, String userId, String sellerID,String itemId, Double itemCost,
			Boolean isExpedited, Double shippingCost, String status, LocalDateTime timestamp) {
		this.paymentId = paymentId;
		this.userId = userId;
		this.itemId = itemId;
		this.itemCost = itemCost;
		this.isExpedited = isExpedited;
		this.shippingCost = shippingCost;
		this.sellerId = sellerID;
		this.status = status;
		this.timestamp = timestamp;
	}

	@Id
    private String id; 
    private String paymentId;  
    private String userId;
    private String itemId;
    private Double itemCost;
    private Boolean isExpedited;
    private Double shippingCost;
    private String buyerFirstName;
    private String buyerLastName;
    private String buyerStreetName;
    private String buyerStreetNum;
    private String buyerCity;
    private String buyerPostalCode;
    private String buyerCountry;
    private String itemName;
    private String sellerId;

    
    // if there was a a failure.
    private String status; 
    private String failureReason; 
    private LocalDateTime timestamp;
    
    
    

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public Double getItemCost() {
		return itemCost;
	}
	public void setItemCost(Double itemCost) {
		this.itemCost = itemCost;
	}
	public Boolean getIsExpedited() {
		return isExpedited;
	}
	public void setIsExpedited(Boolean isExpedited) {
		this.isExpedited = isExpedited;
	}
	public Double getShippingCost() {
		return shippingCost;
	}
	public void setShippingCost(Double shippingCost) {
		this.shippingCost = shippingCost;
	}
	public String getBuyerFirstName() {
		return buyerFirstName;
	}
	public void setBuyerFirstName(String buyerFirstName) {
		this.buyerFirstName = buyerFirstName;
	}
	public String getBuyerLastName() {
		return buyerLastName;
	}
	public void setBuyerLastName(String buyerLastName) {
		this.buyerLastName = buyerLastName;
	}
	public String getBuyerStreetName() {
		return buyerStreetName;
	}
	public void setBuyerStreetName(String buyerStreetName) {
		this.buyerStreetName = buyerStreetName;
	}
	public String getBuyerStreetNum() {
		return buyerStreetNum;
	}
	public void setBuyerStreetNum(String buyerStreetNum) {
		this.buyerStreetNum = buyerStreetNum;
	}
	public String getBuyerCity() {
		return buyerCity;
	}
	public void setBuyerCity(String buyerCity) {
		this.buyerCity = buyerCity;
	}
	public String getBuyerPostalCode() {
		return buyerPostalCode;
	}
	public void setBuyerPostalCode(String buyerPostalCode) {
		this.buyerPostalCode = buyerPostalCode;
	}
	public String getBuyerCountry() {
		return buyerCountry;
	}
	public void setBuyerCountry(String buyerCountry) {
		this.buyerCountry = buyerCountry;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
