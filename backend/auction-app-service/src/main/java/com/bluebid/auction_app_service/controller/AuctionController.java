package com.bluebid.auction_app_service.controller;


import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.bluebid.auction_app_service.dto.NewAuctionRequest;
import com.bluebid.auction_app_service.dto.NewAuctionResponse;
import com.bluebid.auction_app_service.model.Auction;
import com.bluebid.auction_app_service.service.AuctionService;


@RestController
@RequestMapping("auction")
@Validated
public class AuctionController {

	private final AuctionService _auctionService;
	private static final int MAX_CHAR_LIMIT = 400;

	public AuctionController(AuctionService auctionService) {
		this._auctionService = auctionService;

	}
	

	@GetMapping("/auctions/")
	public ResponseEntity<?> getUserAuctions(@RequestHeader(value = "X-User-Id", required = false) String userId) {
	    if (userId == null || userId.isBlank()) {
	        return ResponseEntity.badRequest().body("Missing user id header.");
	    }

	    List<Auction> auctions = _auctionService.getUserAuction(userId);

	    String base = "http://localhost:8080/api";

	    List<EntityModel<Auction>> auctionModels = auctions.stream().map(auction -> {
	        EntityModel<Auction> model = EntityModel.of(auction);
	        model.add(Link.of(base + "/auction/auctions/" + auction.getId()).withSelfRel());
	        model.add(Link.of(base + "/bidding/bids/auction/" + auction.getId()).withRel("bids"));
	        model.add(Link.of(base + "/bidding/bid").withRel("place-bid"));
	        if (auction.getCatalogueID() != null) {
	            model.add(Link.of(base + "/catalogue/items/" + auction.getCatalogueID()).withRel("catalogue-item"));
	        }
	        return model;
	    }).toList();

	    CollectionModel<EntityModel<Auction>> collection = CollectionModel.of(
	            auctionModels,
	            Link.of(base + "/auction/auctions/").withSelfRel()
	    );

	    return ResponseEntity.ok(collection);
	}
	
	@GetMapping("/auctions/to-pay") // auctions a user has won that they have yet to pay for
	public ResponseEntity<?> getUserWonToPayAuctions(
	        @RequestHeader(value = "X-User-Id", required = false) String userId) {

	    if (userId == null || userId.isBlank()) {
	        return ResponseEntity.badRequest().body("Missing user id header.");
	    }

	    List<Auction> auctions = _auctionService.getUserWonAuctions(userId); // get only won-but-not-paid

	    String base = "http://localhost:8080/api";

	    List<EntityModel<Auction>> auctionModels = auctions.stream().map(auction -> {
	        EntityModel<Auction> model = EntityModel.of(auction);

	        // self link to the auction
	        model.add(Link.of(base + "/auction/auctions/" + auction.getId()).withSelfRel());

	        // link to the catalogue item
	        if (auction.getCatalogueID() != null) {
	            model.add(Link.of(base + "/catalogue/items/" + auction.getCatalogueID())
	                    .withRel("catalogue-item"));
	        }

	        // link to payment endpoint for this auction
	        model.add(Link.of(base + "/payment")
	                .withRel("pay-now")
	                .withType("POST"));

	        return model;
	    }).toList();

	    CollectionModel<EntityModel<Auction>> collection = CollectionModel.of(
	            auctionModels,
	            Link.of(base + "/auction/auctions/to-pay").withSelfRel()
	    );

	    return ResponseEntity.ok(collection);
	}

	@PostMapping("/new-auction")
	public ResponseEntity<?> startNewAuction(
	        @RequestBody NewAuctionRequest newAuctionRequest,
	        @RequestHeader(value = "X-User-Id", required = false) String sellerId) {

	    if (sellerId == null || sellerId.isBlank()) {
	        return ResponseEntity.badRequest().body("Missing user id header.");
	    }

	    if (sellerId == null || sellerId.isBlank()) {
			 return ResponseEntity .badRequest().body("Missing user id header.");

		 }
		
		String itemName = newAuctionRequest.getItemName();
		String itemDescription = newAuctionRequest.getItemDescription();
		double basePrice = newAuctionRequest.getBasePrice();
		int secondsDuration = newAuctionRequest.getSeconds();
		
		// these fields will be randomized by the catalogue service upon creation
//		double shippingCost = newAuctionRequest.getShippingCost();
//		double expeditedShippingCost = newAuctionRequest.getExpeditedShippingCost();
//		int shippingDays= newAuctionRequest.getShippingDays();
//		int expeditedShippingDays = newAuctionRequest.getExpeditedShippingDays();
		
		String auctionID = null;

		//initial validations
		if (itemName == null || itemName.isBlank() || itemName.isEmpty())
		{
			return ResponseEntity
					.badRequest()
					.body("Item Name cannot be blank"); 
		}

		if (itemName.length() > MAX_CHAR_LIMIT)
		{
			return ResponseEntity
					.badRequest()
					.body("Item Name is over the character limit of 400"); 
		}

		if (itemDescription.length() > MAX_CHAR_LIMIT)
		{
			return ResponseEntity
					.badRequest()
					.body("Item Name is over the character limit of 400"); 
		}

		if (basePrice < 1)
		{
			return ResponseEntity
					.badRequest()
					.body("Item Price must be greater or equal to $1"); 
		}

		if (secondsDuration < 1)
		{
			return ResponseEntity
					.badRequest()
					.body("Auction duration must be greater or equal to 1 second"); // of course, normally this would be too low. but for testing purposes this timer should be allowed to be set lower
		}
		
		if(secondsDuration > 60 * 60 * 24 * 10) // 10 day limit
		
		{
			return ResponseEntity
					.badRequest()
					.body("Auction duration cannot be longer than 10 days."); // same limit as ebay
			
		}

	    try {
	        auctionID = _auctionService.initiateAuction(
	                newAuctionRequest.getItemName(),
	                newAuctionRequest.getItemDescription(),
	                sellerId,
	                newAuctionRequest.getBasePrice(),
	                newAuctionRequest.getSeconds()
	        );
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	    }

	    NewAuctionResponse response = new NewAuctionResponse(
	            "New auction request has successfully been submitted.",
	            auctionID,
	            true
	    );

	    String base = "http://localhost:8080/api";

	    EntityModel<NewAuctionResponse> model = EntityModel.of(response);
	    model.add(Link.of(base + "/auction/auctions/" + auctionID).withRel("auction"));
	    model.add(Link.of(base + "/auction/auctions/").withRel("all-auctions"));
	    model.add(Link.of(base + "/bidding/bid").withRel("place-bid"));

	    return ResponseEntity.ok(model);
	}


	
	
	
	@GetMapping("/auctions/{auctionid}")
	public ResponseEntity<?> getAuction(@PathVariable String auctionid) {
	    Auction auction = _auctionService.getAuctionById(auctionid);
	    if (auction == null) {
	        return ResponseEntity.notFound().build();
	    }

	    EntityModel<Auction> model = EntityModel.of(auction);
	    String base = "http://localhost:8080/api";

	    model.add(Link.of(base + "/auction/auctions/" + auctionid).withSelfRel());
	    model.add(Link.of(base + "/auction/auctions/").withRel("all-auctions"));
	    model.add(Link.of(base + "/bidding/bids/auction/" + auctionid).withRel("bids"));
	    model.add(Link.of(base + "/bidding/bid").withRel("place-bid"));
	    if (auction.getId() != null) {
	        model.add(Link.of(base + "/catalogue/items/" + auction.getId()).withRel("catalogue-item"));
	    }

	    return ResponseEntity.ok(model);
	}

}
