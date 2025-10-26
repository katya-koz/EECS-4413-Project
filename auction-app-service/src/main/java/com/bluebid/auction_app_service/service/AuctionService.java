
package com.bluebid.auction_app_service.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.bluebid.auction_app_service.dto.AuctionNotification;
import com.bluebid.auction_app_service.model.Auction;
import com.bluebid.auction_app_service.model.Bid;
import com.bluebid.auction_app_service.repository.AuctionRepository;
import com.bluebid.auction_app_service.repository.BidRepository;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class AuctionService {

	private final AuctionRepository _auctionRepository;
	private final BidRepository _bidRepository;
	private final SimpMessagingTemplate  _messagingTemplate;
	//private final SimpMessagingTemplate _messagingTemplate;
	//, SimpMessagingTemplate template
	public AuctionService(AuctionRepository auctionRepository, BidRepository bidRepository, SimpMessagingTemplate  messagingTemplate) {
		this._messagingTemplate = messagingTemplate;
		this._auctionRepository = auctionRepository;
		this._bidRepository = bidRepository;
		
		
	}

  public String placeBid(String auctionId, String userId, Double bidValue) {
	  // check if new bid is higher than current highest bid (or base price)
	  // we need to fetch the current highest bid value
	  List<Bid> bidsOnAuction = _bidRepository.findByAuctionIDOrderByAmountDesc(auctionId);
	  Double currentHighestBidValue = 0.0;
	  
	  if(bidsOnAuction.isEmpty()) {
		  // if there are no bids on the auction, then we are comparing the user's attempted bid against the base price
		  Auction auction = _auctionRepository.findById(auctionId).orElseThrow(() -> new RuntimeException ("Auction not found with ID: " + auctionId));
		  currentHighestBidValue = auction.getBasePrice();
	  }else {
		  currentHighestBidValue = bidsOnAuction.get(0).getAmount();
	  }
	  
	  
	  if(bidValue > currentHighestBidValue) {
		  Bid newBid = new Bid();
		  newBid.setAmount(bidValue);
		  newBid.setAuctionID(auctionId);
		  newBid.setBidTime(LocalDateTime.now());
		  newBid.setBidderID(userId);
		  
		  _bidRepository.save(newBid);
		  
		  return "Bid placed on auction " + auctionId + " for $" + bidValue + " successfully!" ;
	  }
	  
	  return "Bid value too low, must be higher than $" + currentHighestBidValue + ". Bid not placed.";
	  
  }

  public void endAuction(String auctionId) {
	  // assume we have a timer or some external event tracker to track when this ends, then the endpoint is called
	  // this should notify all bidders
	  
	  // first validate that the auction has really ended
	  Auction auction = _auctionRepository.findById(auctionId).orElseThrow(() -> new RuntimeException("Auction not found with ID: " + auctionId));	  
	  
	  if(!auction.getAuctionEndTime().isBefore(LocalDateTime.now())) {
		  // then the auction isnt really over
		  return;
	  }
	  auction.setStatus(false);
	  _auctionRepository.save(auction);
	  
	  // get all bidders by id
	  List<Bid> bids = _bidRepository.findByAuctionIDOrderByAmountDesc(auctionId);

	    if (bids.isEmpty()) {
	        return;
	    }

	    // winner is the highest bid 
	    Bid winner = bids.get(0);

	    // send notifications to all the bidders
	    AuctionNotification notification = new AuctionNotification(
	        "Auction ended! Winner is user " + winner.getBidderID(),
	        auctionId,
	        winner.getAmount(),
	        winner.getBidderID()
	    );
	    
	    _messagingTemplate.convertAndSend("/topic/auction/" + auctionId, notification);
  }
  
  
  public void createNewAuction(Auction auction) {
	_auctionRepository.save(auction);
  }

}
