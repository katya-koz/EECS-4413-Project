package com.bluebid.auction_app_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluebid.auction_app_service.dto.NewAuctionRequest;

@RestController
@RequestMapping("auction")
public class AuctionController {

	
	@PostMapping("/start-new-auction")
	public ResponseEntity<Boolean> startNewAuction(@RequestBody NewAuctionRequest newAuctionRequest){
		return ResponseEntity.ok(true);
		
	}
}
