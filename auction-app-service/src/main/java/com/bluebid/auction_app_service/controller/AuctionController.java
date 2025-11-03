package com.bluebid.auction_app_service.controller;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.bluebid.auction_app_service.dto.InitiateAuctionEvent;
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
	public ResponseEntity<String> startNewAuction(@RequestBody InitiateAuctionEvent newAuctionRequest){

		String itemName = newAuctionRequest.getItemName();
		String itemDescription = newAuctionRequest.getItemDescription();
		String sellerID = newAuctionRequest.getSellerID();
		double basePrice = newAuctionRequest.getBasePrice();
		int days = newAuctionRequest.getDays();
		int hours = newAuctionRequest.getHours();
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

		if (days < 0)
		{
			return ResponseEntity
					.badRequest()
					.body("Auction duration must be greater or equal to 1 hour"); 
		}

		if (hours < 0)
		{
			return ResponseEntity
					.badRequest()
					.body("Auction duration must be greater or equal to 1 hour"); 
		}


		//attempt to initiate auction
		try
		{
			auctionID = _auctionService.initiateAuction(itemName, itemDescription, sellerID, false, basePrice, days, hours);
		}
		
		catch(IllegalArgumentException e)
		{
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(e.getMessage());

		}

		//if all validations pass, return an auction submited succesfully message
		//add a new response object (similar to what you loked at before - to implment)
		return new ResponseEntity<>(
				String.format("Auction %s submitted successfully", auctionID), 
				HttpStatus.CREATED
				);
	}

	//  @PostMapping
	//  public ResponseEntity<Auction> create(@Valid @RequestBody CreateAuctionRequest auction) {
	//    Auction a = new Auction(auction.getSellerId(), auction.getCatalogueId(), auction.getItemName(), auction.getBasePrice(), auction.getAuctionType(), auction.getShippingCost(), auction.getExpeditedShippingCost(), auction.getShippingDays(), auction.getEndTime());
	//    a = Repo.save(a);
	//    return ResponseEntity.created(URI.create("/api/auctions/" + a.getId())).body(a);
	//  }
	//
	//  @GetMapping
	//  public List<Auction> list() { return Repo.findAll(); }
	//
	//  @GetMapping("/{id}")
	//  public ResponseEntity<Auction> get(@PathVariable String id) {
	//    return Repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	//  }
	//
	//  @PostMapping("/{id}/bids")
	//  public ResponseEntity<?> bid(@PathVariable String id, @Valid @RequestBody BidRequest bid) {
	//    Optional<Auction> updated = auctionService.placeBid(id, bid.getUserId(), bid.getBidValue());
	//    if (updated.isPresent()) {
	//      return ResponseEntity.ok(updated.get());
	//    } else {
	//      return ResponseEntity.badRequest().body("Bid rejected: lower than current highest or auction not active");
	//    }
	//  }
	//
	//  @PostMapping("/{id}/end")
	//  public ResponseEntity<?> end(@PathVariable String id) {
	//    Optional<Auction> ended = auctionService.endAuction(id);
	//    if (ended.isPresent()) {
	//      notificationService.sendAuctionEndNotification(id, ended.get().getHighestBidderId());
	//      return ResponseEntity.ok(ended.get());
	//    } else {
	//      return ResponseEntity.notFound().build();
	//    }
	//  }
}
