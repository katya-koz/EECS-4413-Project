package com.bluebid.auction_app_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bluebid.auction_app_service.model.Auction;

public interface AuctionRepository extends MongoRepository<Auction, String> {

	List<Auction> findByStatusTrueAndAuctionEndTimeBefore(LocalDateTime now);

	Optional<Auction> findByIdAndStatus(String auctionID, boolean b);

}
