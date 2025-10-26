package com.bluebid.catalogue_app_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "items") // part of items collection
public class CatalogueItem {

//    @Id
//    private String itemID; // mongo id
    private String sellerID;
    private String highestBidderID;
    private String itemName;
    private double currentBiddingPrice;
    private String auctionType;
    private LocalDateTime auctionStartDate;
    private LocalDateTime auctionEndDate;  
    private double shippingCost;
    private double expeditedShippingCost;
    private String itemDescription;
    private int shippingDays;

    
    public CatalogueItem(String itemName, LocalDateTime auctionEnd) {
    	this.itemName = itemName;
    	this.auctionEndDate = auctionEnd;
    	
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getHighestBidderID() {
        return highestBidderID;
    }

    public void setHighestBidderID(String highestBidderID) {
        this.highestBidderID = highestBidderID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getCurrentBiddingPrice() {
        return currentBiddingPrice;
    }

    public void setCurrentBiddingPrice(double currentBiddingPrice) {
        this.currentBiddingPrice = currentBiddingPrice;
    }

    public String getAuctionType() {
        return auctionType;
    }

    public void setAuctionType(String auctionType) {
        this.auctionType = auctionType;
    }

    public LocalDateTime getAuctionEndTime() {
        return auctionEndDate;
    }

    public void setAuctionEndTime(LocalDateTime auctionEndDate){
        this.auctionEndDate = auctionEndDate;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public double getExpeditedShippingCost() {
        return expeditedShippingCost;
    }

    public void setExpeditedShippingCost(double expeditedShippingCost) {
        this.expeditedShippingCost = expeditedShippingCost;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getShippingDays() {
        return shippingDays;
    }

    public void setShippingDays(int shippingDays) {
        this.shippingDays = shippingDays;
    }

	public LocalDateTime getAuctionStartDate() {
		return auctionStartDate;
	}

	public void setAuctionStartDate(LocalDateTime auctionStartDate) {
		this.auctionStartDate = auctionStartDate;
	}
}
