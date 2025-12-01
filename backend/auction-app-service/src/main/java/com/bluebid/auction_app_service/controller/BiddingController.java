package com.bluebid.auction_app_service.controller;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
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
	
	//attempt to bid. if bid is valid (over highest bid or higher than base price - if there are no bids), then save to database. return bool based on database save status
	@PostMapping("/bid")
	public ResponseEntity<?> attemptBid(
	        @RequestBody AttemptBidRequest attemptBidRequest,
	        @RequestHeader(value = "X-User-Id", required = false) String userId) {

	    if (userId == null || userId.isBlank()) {
	        return ResponseEntity.badRequest().body("Missing user id header.");
	    }

	    BidResponse response = _auctionService.initiatePlaceBid(
	            attemptBidRequest.getAuctionID(),
	            userId,
	            attemptBidRequest.getBidRequest()
	    );

	    EntityModel<BidResponse> model = EntityModel.of(response);

	    String base = "http://localhost:8080/api";
	    
	    model.add(Link.of(base + "/bidding/bid").withSelfRel());

	    // related auction
	    model.add(Link.of(base + "/auction/auctions/" + attemptBidRequest.getAuctionID())
	            .withRel("auction"));

	    // view the bid (only if bidid is returned)
	    if (response.getBidId() != null) {
	        model.add(Link.of(base + "/bidding/bids/" + response.getBidId())
	                .withRel("view-bid"));
	    }

	    return ResponseEntity.ok(model);
	}
	
	

	@GetMapping("/bids/{bidId}")
	public ResponseEntity<?> getReceipt(@PathVariable String bidId) {
	    Bid bid = _auctionService.getBidById(bidId);

	    if (bid == null) {
	        return ResponseEntity.notFound().build();
	    }

	    EntityModel<Bid> model = EntityModel.of(bid);

	    String base = "http://localhost:8080/api";

	    // self
	    model.add(Link.of(base + "/bidding/bids/" + bidId).withSelfRel());

	    // related auction
	    model.add(Link.of(base + "/auction/auctions/" + bid.getAuctionID())
	            .withRel("auction"));

	    // place another bid
	    model.add(Link.of(base + "/bidding/bid").withRel("place-bid"));

	    // catalogue item link
	    if (bid.getCatalogueID() != null) {
	        model.add(Link.of(base + "/catalogue/items/" + bid.getCatalogueID())
	                .withRel("catalogue-item"));
	    }

	    return ResponseEntity.ok(model);
	}

}

