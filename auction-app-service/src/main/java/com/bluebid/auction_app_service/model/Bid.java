package com.bluebid.auction_app_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "bids")
public class Bid {

    @Id
    private String id; // bid id in mongo (primary key)
    private String auctionID;
    private String bidderID;
    private double amount;
    private LocalDateTime bidTime;
    
    public String getId() {
        return id;
    }

//    public void setId(String id) {
//        this.id = id;
//    }

    public String getAuctionID() {
        return auctionID;
    }

    public void setAuctionID(String auctionID) {
        this.auctionID = auctionID;
    }

    public String getBidderID() {
        return bidderID;
    }

    public void setBidderID(String bidderID) {
        this.bidderID = bidderID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getBidTime() {
        return bidTime;
    }

    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }
}