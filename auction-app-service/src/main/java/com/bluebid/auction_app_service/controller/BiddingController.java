package com.bluebid.auction_app_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluebid.auction_app_service.dto.AttemptBidRequest;
import com.bluebid.auction_app_service.service.AuctionService;




@RestController
@RequestMapping("bidding")
public class BiddingController {
	
	private final AuctionService _auctionService;
	
	public BiddingController(AuctionService auctionService) {
		this._auctionService = auctionService;
	}
	
	@PostMapping("/place-bid")
	public ResponseEntity<String> attemptBid(@RequestBody AttemptBidRequest attemptBidRequest) {
		//attempt to bid. if bid is valid (over highest bid or higher than base price - if there are no bids), then save to database. return bool based on database save status
		
		String message = _auctionService.placeBid(attemptBidRequest.getAuctionID(), attemptBidRequest.getUserID(), attemptBidRequest.getBidRequest());
		
	    return ResponseEntity.ok(message);
	}

}
