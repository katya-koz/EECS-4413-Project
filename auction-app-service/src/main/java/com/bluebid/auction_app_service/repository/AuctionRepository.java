package com.bluebid.auction_app_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bluebid.auction_app_service.model.Auction;

public interface AuctionRepository extends MongoRepository<Auction, String> {

}
