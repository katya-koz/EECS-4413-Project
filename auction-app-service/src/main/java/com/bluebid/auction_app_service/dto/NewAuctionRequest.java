package com.bluebid.auction_app_service.dto;

import java.time.LocalDateTime;
public class NewAuctionRequest {
	
	String sellerID;
	String catalogueID;
	String basePrice;
	LocalDateTime auctionStartTime;
	LocalDateTime auctionEndTime;

}
