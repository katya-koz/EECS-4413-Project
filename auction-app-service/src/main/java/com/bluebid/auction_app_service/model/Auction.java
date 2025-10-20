package com.example.auction.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "auctions")
@CompoundIndex(def = "{'seller': 1, 'endTime': 1}")
public class Auction {
  private String id;
  private String seller;
  private String catalogueId;
  private String item;
  private int basePrice;
  private String auctionType;
  private int shippingCost;
  private int expeditedShippingCost;
  private Integer shippingDays;
  private Instant endTime;

  private String status; 
  private String highestBidderId;
  private int highestBid;
  private Integer bidCount;

  public Auction() {} 

  public Auction(String seller, String catalogueId, String item, int basePrice, String auctionType, int shippingCost, int expeditedShippingCost, Integer shippingDays, Instant endTime) {
    this.seller = seller;
    this.catalogueId = catalogueId;
    this.item = item;
    this.basePrice = basePrice;
    this.auctionType = auctionType;
    this.shippingCost = shippingCost;
    this.expeditedShippingCost = expeditedShippingCost;
    this.shippingDays = shippingDays;
    this.endTime = endTime;
    this.status = "ACTIVE";
    this.highestBid = basePrice;
    this.bidCount = 0;
  }

  public String getId() { 
    return id; }
  public void setId(String id) { 
    this.id = id; }
  public String getseller() { 
    return seller; }
  public void setseller(String seller) { 
    this.seller = seller; }
  public String getCatalogueId() { 
    return catalogueId; }
  public void setCatalogueId(String catalogueId) { 
    this.catalogueId = catalogueId; }
  public String getitem() { 
    return item; }
  public void setitem(String item) { 
    this.item = item; }
  public int getBasePrice() { 
    return basePrice; }
  public void setBasePrice(int basePrice) { 
    this.basePrice = basePrice; }
  public String getAuctionType() { 
    return auctionType; }
  public void setAuctionType(String auctionType) { 
    this.auctionType = auctionType; }
  public int getShippingCost() { 
    return shippingCost; }
  public void setShippingCost(int shippingCost) { 
    this.shippingCost = shippingCost; }
  public int getExpeditedShippingCost() { 
    return expeditedShippingCost; }
  public void setExpeditedShippingCost(int expeditedShippingCost) { 
    this.expeditedShippingCost = expeditedShippingCost; }
  public Integer getShippingDays() { 
    return shippingDays; }
  public void setShippingDays(Integer shippingDays) { 
    this.shippingDays = shippingDays; }
  public Instant getEndTime() { 
    return endTime; }
  public void setEndTime(Instant endTime) { 
    this.endTime = endTime; }
  public String getStatus() { 
    return status; }
  public void setStatus(String status) { 
    this.status = status; }
  public String getHighestBidderId() { 
    return highestBidderId; }
  public void setHighestBidderId(String highestBidderId) { 
    this.highestBidderId = highestBidderId; }
  public int getHighestBid() { 
    return highestBid; }
  public void setHighestBid(int highestBid) { 
    this.highestBid = highestBid; }
  public Integer getBidsCount() { 
    return bidCount; }
  public void setBidsCount(Integer bidCount) { 
    this.bidCount = bidCount; }
}
