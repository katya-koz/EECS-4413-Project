package com.example.auction.model;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.validation.constraints.*;

public class CreateAuctionRequest {
  private String seller;
  private String catalogueId;
  private String item;
  @DecimalMin("0.0")
  private BigDecimal basePrice;
  private String auctionType;
  @DecimalMin("0.0")
  private BigDecimal shippingCost;
  @DecimalMin("0.0")
  private BigDecimal expeditedShippingCost;
  @Min(0)
  private Integer shippingDays;
  private Instant endTime;

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
  public BigDecimal getBasePrice() { 
    return basePrice; }
  public void setBasePrice(BigDecimal basePrice) { 
    this.basePrice = basePrice; }
  public String getAuctionType() { 
    return auctionType; }
  public void setAuctionType(String auctionType) { 
    this.auctionType = auctionType; }
  public BigDecimal getShippingCost() { 
    return shippingCost; }
  public void setShippingCost(BigDecimal shippingCost) { 
    this.shippingCost = shippingCost; }
  public BigDecimal getExpeditedShippingCost() { 
    return expeditedShippingCost; }
  public void setExpeditedShippingCost(BigDecimal expeditedShippingCost) { 
    this.expeditedShippingCost = expeditedShippingCost; }
  public Integer getShippingDays() { 
    return shippingDays; }
  public void setShippingDays(Integer shippingDays) { 
    this.shippingDays = shippingDays; }
  public Instant getEndTime() { 
    return endTime; }
  public void setEndTime(Instant endTime) { 
    this.endTime = endTime; }
}
