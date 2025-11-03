package com.bluebid.auction_app_service.controller;

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

	//  @Autowired AuctionRepository Repo;
	//  @Autowired AuctionService auctionService;
	//  @Autowired NotificationService notificationService;
	// maybe we use these for dep inj?

	private final AuctionService _auctionService;
	private static final int MAX_CHAR_LIMIT = 400;

	public AuctionController(AuctionService auctionService) {
		this._auctionService = auctionService;

	}

	@PostMapping("/new-auction")
	public ResponseEntity<?> startNewAuction(@RequestBody NewAuctionRequest newAuctionRequest){

		String itemName = newAuctionRequest.getItemName();
		String itemDescription = newAuctionRequest.getItemDescription();
		String sellerID = newAuctionRequest.getSellerID();
		double basePrice = newAuctionRequest.getBasePrice();
		int secondsDuration = newAuctionRequest.getSeconds();
		
		// these fields will be randomized by the catalogue service upon creation
//		double shippingCost = newAuctionRequest.getShippingCost();
//		double expeditedShippingCost = newAuctionRequest.getExpeditedShippingCost();
//		int shippingDays= newAuctionRequest.getShippingDays();
//		int expeditedShippingDays = newAuctionRequest.getExpeditedShippingDays();
		
		String auctionID = null;

		//initial validations
		if (itemName == null)
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
		


		//attempt to initiate auction
		try
		{
			auctionID = _auctionService.initiateAuction(itemName, itemDescription, sellerID,basePrice, secondsDuration);
		}
		
		catch(IllegalArgumentException e)
		{
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(e.getMessage());

		}

		 return ResponseEntity.ok(new NewAuctionResponse("New auction request has successfully been submitted.", auctionID, true));
	}
	
	
	@GetMapping("/auctions/{auctionid}")
	public ResponseEntity<?> getAuction(@PathVariable String auctionid) {
	    Auction receipt = _auctionService.getAuctionById(auctionid);
	    if (receipt != null) {
	        return ResponseEntity.ok(receipt);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

}
