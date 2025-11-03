//package com.bluebid.auction_app_service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import com.bluebid.auction_app_service.model.Auction;
//import com.bluebid.auction_app_service.repository.AuctionRepository;
//
//// end auctions every 10 seconds
//@Service
//public class AuctionScheduler {
//
//    
//    private final AuctionRepository _auctionRepository;
//    
//    public AuctionScheduler(AuctionRepository auctionRepo) {
//    	this._auctionRepository = auctionRepo;
//    }
//
//    @Scheduled(fixedRate = 10 * 1000) // every 10 seconds for testing purposes
//    public void closeExpiredAuctions() {
//        LocalDateTime now = LocalDateTime.now();
//        List<Auction> expiredAuctions = _auctionRepository.findByStatusTrueAndAuctionEndTimeBefore(now);
//
//        for (Auction auction : expiredAuctions) {
//            auction.setStatus(false);
//            auction.setAuctionStatus("closed");
//            _auctionRepository.save(auction);
//
//        }
//    }
//}