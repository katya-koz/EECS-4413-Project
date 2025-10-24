package com.bluebid.auction_app_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bluebid.auction_app_service.model.Bid;

public interface BidRepository extends MongoRepository<Bid, String>{


	// return list of bids by auction id in descending order - highest bid is at the top
	List<Bid> findByAuctionIDOrderByAmountDesc(String auctionId);

}


