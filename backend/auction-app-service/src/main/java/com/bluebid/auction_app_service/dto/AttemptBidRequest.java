package com.bluebid.auction_app_service.dto;

import java.time.LocalDateTime;

public class AttemptBidRequest {
	
	//String userID;
	String auctionID;
	double bidRequest;
	LocalDateTime bidSentTime;
	
	public String getAuctionID() {
		return auctionID;
	}
	public void setAuctionID(String auctionID) {
		this.auctionID = auctionID;
	}
	
	
//	public String getUserID() {
//		return userID;
//	}
//	public void setUserID(String userID) {
//		this.userID = userID;
//	}
	
	public double getBidRequest() {
		return bidRequest;
	}
	public void setBidRequest(double bidRequest) {
		this.bidRequest = bidRequest;
	}
	public LocalDateTime getBidSentTime() {
		return bidSentTime;
	}
	public void setBidSentTime(LocalDateTime bidSentTime) {
		this.bidSentTime = bidSentTime;
	}


}
