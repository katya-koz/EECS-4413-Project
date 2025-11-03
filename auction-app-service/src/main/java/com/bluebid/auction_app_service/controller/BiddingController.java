package com.bluebid.auction_app_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluebid.auction_app_service.dto.AttemptBidRequest;
import com.bluebid.auction_app_service.service.AuctionService;
import com.bluebid.auction_app_service.dto.BidResponse;
import com.bluebid.auction_app_service.model.Bid;




@RestController
@RequestMapping("bidding")
public class BiddingController {
	
	private final AuctionService _auctionService;
	
	public BiddingController(AuctionService auctionService) {
		this._auctionService = auctionService;
	}
	
	@PostMapping("/bid")
	public ResponseEntity<?> attemptBid(@RequestBody AttemptBidRequest attemptBidRequest, @RequestHeader(value = "X-User-Id", required = false) String userId) {
		//attempt to bid. if bid is valid (over highest bid or higher than base price - if there are no bids), then save to database. return bool based on database save status
		
		 if (userId == null || userId.isBlank()) {
			 return ResponseEntity.badRequest().body("Missing user id header.");

		 }
		
		BidResponse response = _auctionService.initiatePlaceBid(attemptBidRequest.getAuctionID(), userId, attemptBidRequest.getBidRequest());
		
	    return ResponseEntity.ok(response);
	}
	
	

	@GetMapping("/bids/{bidId}")
	public ResponseEntity<?> getReceipt(@PathVariable String bidId) {
	    Bid bid = _auctionService.getBidById(bidId);
	    if (bid != null) {
	        return ResponseEntity.ok(bid);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

}


/*
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */