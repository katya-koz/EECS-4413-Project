package com.bluebid.auction_app_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluebid.auction_app_service.dto.AttemptBidRequest;




@RestController
@RequestMapping("bidding")
public class BiddingController {
	
	@PostMapping("/attempt-bid")
	public ResponseEntity<Boolean> attemptBid(@RequestBody AttemptBidRequest attemptBidRequest) {
	
	   // attempt to bid. if bid is valid (over highest bid), then save to database. return bool based on database save status
	    return ResponseEntity.ok(true);
	}

}
