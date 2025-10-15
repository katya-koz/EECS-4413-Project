package com.bluebid.catalogue_app_service.dto;

import java.time.LocalDateTime;

public class PostNewItemRequest {
	String sellerID;
	LocalDateTime postedDate;
	LocalDateTime auctionEndDate;
	String catalogueID;
	String basePrice;

}
