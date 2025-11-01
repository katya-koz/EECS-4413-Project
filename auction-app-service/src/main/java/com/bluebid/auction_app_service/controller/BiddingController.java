package com.bluebid.auction_app_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public ResponseEntity<BidResponse> attemptBid(@RequestBody AttemptBidRequest attemptBidRequest) {
		//attempt to bid. if bid is valid (over highest bid or higher than base price - if there are no bids), then save to database. return bool based on database save status
		
		BidResponse response = _auctionService.initiatePlaceBid(attemptBidRequest.getAuctionID(), attemptBidRequest.getUserID(), attemptBidRequest.getBidRequest());
		
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
