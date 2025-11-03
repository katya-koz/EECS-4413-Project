
package com.bluebid.auction_app_service.service;

import org.apache.kafka.common.metrics.stats.Max;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.bluebid.auction_app_service.dto.AuctionNotification;
import com.bluebid.auction_app_service.dto.BidInitiatedEvent;
import com.bluebid.auction_app_service.dto.BidResponse;
import com.bluebid.auction_app_service.dto.ItemValidationFailureEvent;
import com.bluebid.auction_app_service.dto.ItemValidationSuccessEvent;
import com.bluebid.auction_app_service.dto.InitiateAuctionEvent;
import com.bluebid.auction_app_service.dto.ItemAddedFailureEvent;
import com.bluebid.auction_app_service.dto.ItemAddedSuccessEvent;
import com.bluebid.auction_app_service.dto.UserInfoValidationFailureEvent;
import com.bluebid.auction_app_service.dto.UserInfoValidationSuccessEvent;
import com.bluebid.auction_app_service.model.Auction;
import com.bluebid.auction_app_service.model.Bid;
import com.bluebid.auction_app_service.repository.AuctionRepository;
import com.bluebid.auction_app_service.repository.BidRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



@Service
public class AuctionService {
	
	private static final int MAX_CHAR_LIMIT = 400;

	private final AuctionRepository _auctionRepository;
	private final BidRepository _bidRepository;
	private final SimpMessagingTemplate  _messagingTemplate;
	//private final SimpMessagingTemplate _messagingTemplate;
	//, SimpMessagingTemplate template
	private final KafkaTemplate<String, Object> _kafkaTemplate;

	public AuctionService(AuctionRepository auctionRepository, BidRepository bidRepository, SimpMessagingTemplate  messagingTemplate,KafkaTemplate<String, Object> kafkaTemplate) {
		this._messagingTemplate = messagingTemplate;
		this._auctionRepository = auctionRepository;
		this._bidRepository = bidRepository;
		this._kafkaTemplate = kafkaTemplate;


	}

	public BidResponse initiatePlaceBid(String auctionId, String userId, Double bidValue) {
		// check if new bid is higher than current highest bid (or base price)
		// we need to fetch the current highest bid value
		List<Bid> bidsOnAuction = _bidRepository.findByAuctionIDAndIsValidTrueOrderByAmountDesc(auctionId);
		Double currentHighestBidValue = 0.0;
		Auction auction = _auctionRepository.findById(auctionId).orElseThrow(() -> new RuntimeException ("Auction not found with ID: " + auctionId));
		if(bidsOnAuction.isEmpty()) {
			// if there are no bids on the auction, then we are comparing the user's attempted bid against the base price

			currentHighestBidValue = auction.getBasePrice();
		}else {
			currentHighestBidValue = bidsOnAuction.get(0).getAmount();
		}


		// round highest bid down to 2 decimal places

		currentHighestBidValue = Math.round(currentHighestBidValue * 100.0) / 100.0;

		if(bidValue > currentHighestBidValue) {
			Bid newBid = new Bid();
			newBid.setAmount(bidValue);
			newBid.setAuctionID(auctionId);
			newBid.setBidTime(LocalDateTime.now());
			newBid.setBidderID(userId);
			newBid.setValid(false); // false for now, until we can validate the user and catalogue item

			_bidRepository.save(newBid);


			// trigger catalogue to update item bidding price
			_kafkaTemplate.send("bid.user-validation-topic", new BidInitiatedEvent(newBid.getId(), userId, auction.getCatalogueID(), LocalDateTime.now(), bidValue )); // id will be same as bid id in db

			return new BidResponse(auction.getCatalogueID(), userId, bidValue, auctionId, newBid.getId(),"Bid request placed on auction " + auctionId + " for $" + bidValue + " successfully!", true ); 
		}

		// we dont save bids that fail here
		return new BidResponse(auction.getCatalogueID(), userId, bidValue, auctionId, null,"Bid value too low, must be higher than $" + currentHighestBidValue + ". Bid not placed.", false); //"Bid value too low, must be higher than $" + currentHighestBidValue + ". Bid not placed.";

	}


	@KafkaListener(topics = "catalogue.bid-item-validation-success-topic", groupId = "bid-group", containerFactory = "itemValidationSuccessListenerFactory")
	public void handleCatalogueSuccess( ItemValidationSuccessEvent event) {
		Optional<Bid> optionalReceipt = _bidRepository.findById(event.getProducerEventID());

		Bid bid = optionalReceipt.get(); 

		// update or overwrite cat. info
		bid.setStatus("Bid sucessful.");

		// this is the last step int he saga, so if this is successful, than the bid is valid
		bid.setValid(true);
		// save
		_bidRepository.save(bid);

	}

	@KafkaListener(topics = "catalogue.bid-item-validation-failed-topic", groupId = "bid-group", containerFactory = "itemValidationFailureListenerFactory")
	public void handleCatalogueFailure( ItemValidationFailureEvent event) {
		Optional<Bid> optionalBid = _bidRepository.findById(event.getProducerEventID());

		Bid bid = optionalBid.get(); 

		// update status
		bid.setStatus(event.getMessage());
		bid.setValid(false);
		// save
		_bidRepository.save(bid);
	}
	//
	@KafkaListener(topics = "user.bid-user-validation-success-topic", groupId = "bid-group", containerFactory = "userValidationSuccessListenerFactory")
	public void handleUserSuccess(UserInfoValidationSuccessEvent event) {
		Optional<Bid> optionalBid = _bidRepository.findById(event.getProducerID());

		Bid bid = optionalBid.get();

		// update or overwrite bid info
		bid.setStatus("USER_VALIDATED");
		bid.setValid(true);

		// save
		_bidRepository.save(bid);

		// once user is validated, then we can send a catalogue validation request.
		_kafkaTemplate.send("bid.item-validation-topic", new BidInitiatedEvent(bid.getId(), bid.getBidderID(), bid.getCatalogueID(), bid.getBidTime(), bid.getAmount() )); // can turn this into a copy construtor


	}

	@KafkaListener(topics = "user.bid-validation-failed-topic", groupId = "bid-group" , containerFactory = "userValidationFailureListenerFactory")
	public void handleUserFailure(UserInfoValidationFailureEvent event) {
		Optional<Bid> optionalBid = _bidRepository.findById(event.getProducerEventID());
		Bid bid = optionalBid.get();

		// update or overwrite bid info
		bid.setStatus(event.getMessage());
		bid.setValid(false);


		// save
		_bidRepository.save(bid);
	}

	
	@KafkaListener(topics = "catalogue.item-add-success-topic", groupId = "item-upload-group", containerFactory = "itemAddedSuccessListenerFactory")
	public void handleItemAddSuccess(ItemAddedSuccessEvent event) 
	{
		String auctionID = event.getAuctionID();
		
		Optional<Auction> auctionOptional = _auctionRepository.findById(auctionID);
		
		Auction auction = auctionOptional.get();
		
		//update the status
		auction.setAuctionStatus("item validated");
		auction.setValid(true);
		
		//save
		_auctionRepository.save(auction);
	}
	
	@KafkaListener(topics = "catalogue.item-add-failure-topic", groupId = "item-upload-group", containerFactory = "itemAddedFailureListenerFactory")
	public void handleItemAddSuccess(ItemAddedFailureEvent event) 
	{
		String auctionID = event.getAuctionID();
		
		Optional<Auction> auctionOptional = _auctionRepository.findById(auctionID);
		
		Auction auction = auctionOptional.get();
		
		//update the status
		auction.setAuctionStatus("item not validated. invalid item");
		auction.setValid(false);
		
		//save
		_auctionRepository.save(auction);
	}
	
	
	//logical validation methods
	private boolean validateAuctionInput(String itemName, String itemDescription, String sellerID, boolean isValid, double basePrice, int days, int hours)
	{
	
		if (itemName == null)
		{
			return false;
		}
		if (itemName.length() > MAX_CHAR_LIMIT)
		{
			return false;
		}
		if (itemDescription.length() > MAX_CHAR_LIMIT)
		{
			return false;
		}
		if (basePrice < 1)
		{
			return false;
		}
		return true;	
	
	}
	
	public String isValidAuctionItem(String itemName, String itemDescription, String sellerID, boolean isValid, double basePrice, int days, int hours)
	{
		boolean areItemsValid = validateAuctionInput(itemName, itemDescription, sellerID, isValid, basePrice, days, hours);
		
		if (areItemsValid == true)
		{
			return initiateAuction(itemName, itemDescription, sellerID, isValid, basePrice, days, hours);
		}
		else
		{
			return "Please check your inputs and try again.";
		}
		
	}
	
	public String initiateAuction(String itemName, String itemDescription, String sellerID, boolean isValid, double basePrice, int days, int hours)
	{
		//create initiateAuctionEvent
		InitiateAuctionEvent event = new InitiateAuctionEvent(itemName, itemDescription, sellerID, isValid, basePrice, days, hours);
		
		//create Auction item
		Auction auction = new Auction(
				itemName,
				itemDescription,
				sellerID,
				false,
				basePrice,
				days,
				hours
				);
		
		auction.setAuctionStartTime(LocalDateTime.now());
		//calculate auction duration and set the auction end time
		auction.setAuctionEndTime(auction.calcuateDuration());
		
		auction.setAuctionStatus("pending validation");
		
		//save the auction into the db (with inital state of isValid = false)
		_auctionRepository.save(auction);
		
		
		//publish event for user
		_kafkaTemplate.send("auction.validation-topic", event);
		
		return auction.getAuctionID();
		
		
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

	public Bid getBidById(String bidId) {
		return _bidRepository.findById(bidId).orElse(null);
	}

//	private Auction convertToAuctionObject(InitiateAuctionEvent request)
//	{
//		Auction auction = new Auction();
//		auction.setSellerID(request.getSellerID());
//		auction.setCatalogueID(request.getCatalogueID());
//		auction.setAuctionType("FORWARD");
//
//		String priceAsString = request.getBasePrice();
//		auction.setBasePrice(Integer.parseInt(priceAsString));
//
//		return auction;
//	}

}
