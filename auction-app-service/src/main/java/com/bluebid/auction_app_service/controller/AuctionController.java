package com.bluebid.auction_app_service.controller;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.bluebid.auction_app_service.dto.NewAuctionRequest;
import com.bluebid.auction_app_service.model.Auction;
import com.bluebid.auction_app_service.service.AuctionService;


@RestController
@RequestMapping("/api/auction")
@Validated
public class AuctionController {

//  @Autowired AuctionRepository Repo;
//  @Autowired AuctionService auctionService;
//  @Autowired NotificationService notificationService;
	// maybe we use these for dep inj?
	
	private final AuctionService _auctionService;
	
	public AuctionController(AuctionService auctionService) {
		this._auctionService = auctionService;
		
	}
	
	
	@PostMapping("/new-auction")
	public ResponseEntity<?> startNewAuction(@RequestBody NewAuctionRequest newAuctionRequest,
												@RequestHeader(value = "X-User-Id", required = false) String sellerId){
		
		if (sellerId == null || sellerId.isBlank()) {
			 return ResponseEntity.badRequest().body("Missing user id header.");
		 }
		
		
		LocalDateTime startTime = LocalDateTime.now();
		LocalDateTime endTime = newAuctionRequest.getAuctionEndTime();
			 
		Auction auction = new Auction();
		auction.setSellerID(sellerId);
		auction.setCatalogueID(newAuctionRequest.getCatalogueID());
		auction.setAuctionStartTime(startTime);
		auction.setAuctionEndTime(endTime);
		auction.setStatus(true); 
		
		
		_auctionService.createNewAuction(auction);
		return ResponseEntity.ok(true);
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
