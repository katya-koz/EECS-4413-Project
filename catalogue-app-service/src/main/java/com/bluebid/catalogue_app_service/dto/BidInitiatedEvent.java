package com.bluebid.catalogue_app_service.dto;

import java.time.LocalDateTime;

import org.apache.kafka.common.Uuid;

public class BidInitiatedEvent {
	// publish this event when a bid request has been made
	private String id; 
	private String userID; // user making purchase
	private String catalogueID; // item id in catalogue
	private LocalDateTime bidTime;
	private Double bidAmount;

//	public BidInitiatedEvent(String userID,String catalogueID, LocalDateTime bidTime, Double bidAmount) {
//		this.userID = userID;
//		this.catalogueID = catalogueID;
//		this.bidTime = bidTime;
//		this.setBidAmount(bidAmount);
//		this.setId(Uuid.randomUuid().toString());
//	}
	
	public BidInitiatedEvent(String id, String userID,String catalogueID, LocalDateTime bidTime, Double bidAmount) {
		this.userID = userID;
		this.catalogueID = catalogueID;
		this.bidTime = bidTime;
		this.setBidAmount(bidAmount);
		this.setId(id);
	}
	public BidInitiatedEvent() {} // json deserial

	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getCatalogueID() {
		return catalogueID;
	}
	public void setCatalogueID(String catalogueID) {
		this.catalogueID = catalogueID;
	}
	public LocalDateTime getBidTime() {
		return bidTime;
	}
	public void setBidTime(LocalDateTime paymentTime) {
		this.bidTime = paymentTime;
	}

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

	public Double getBidAmount() {
		return bidAmount;
	}

	public void setBidAmount(Double bidAmount) {
		this.bidAmount = bidAmount;
	}

}
