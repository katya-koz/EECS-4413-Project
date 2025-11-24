package com.bluebid.auction_app_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.bluebid.auction_app_service.dto.AuctionNotification;
import com.bluebid.auction_app_service.model.Auction;
import com.bluebid.auction_app_service.model.Bid;
import com.bluebid.auction_app_service.repository.AuctionRepository;
import com.bluebid.auction_app_service.repository.BidRepository;

@Service
public class AuctionNotificationService {
	
	private final SimpMessagingTemplate _msgTemplate;
	private AuctionRepository _auctionRepository;
	private BidRepository _bidRepository;
	
	public AuctionNotificationService(SimpMessagingTemplate msgTemplate,AuctionRepository auctionRepository, BidRepository bidRepository) {
		this._msgTemplate = msgTemplate;
		this._auctionRepository = auctionRepository;
		this._bidRepository = bidRepository;
	}

	
	// on auction end event, notify subscribers
	public void notifyAuctionEnd(Auction auction) {
	
		// get the list of bidders on an auction
		List<Bid> bids = _bidRepository.findByAuctionIDOrderByAmountDesc(auction.getId());
		if (bids.isEmpty()) {
			return;
		}

		// winner is the highest bid 
		Bid winner = bids.get(0);
		AuctionNotification notification = new AuctionNotification(
				"Auction ended! Winner is user " + winner.getBidderID(),
				auction,
				winner.getAmount(),
				winner.getBidderID()
				);

		// send the same message to all bidders. since we are sending the winnerid, the front end will display a different message based on if the winner is logged in.
		// this is okay to do since the requirements want us to display the winner id for everyone, and also because our backend will verify that the corrrect user is attempting to pay/claim 
		// an item with the json token.
		// i also thought it would be more efficient for the server to publish one topic per auction rather than a topic per every user per auction.
		_msgTemplate.convertAndSend("/topic/auction/" + auction.getId(), notification);
	}
	
	public void notifyTest() {
		_msgTemplate.convertAndSend("/topic/auction/test", "hello");
	}

}

