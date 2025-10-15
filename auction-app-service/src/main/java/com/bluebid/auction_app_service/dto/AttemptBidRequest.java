package com.bluebid.auction_app_service.dto;

import java.time.LocalDateTime;

public class AttemptBidRequest {
	String userID;
	String catalogueID;
	double currentHighestBid;
	LocalDateTime bidSentTime;

}
