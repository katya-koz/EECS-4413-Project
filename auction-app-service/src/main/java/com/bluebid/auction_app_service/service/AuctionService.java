package com.example.auction.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.auction.model.Auction;

@Service
public class AuctionService {

  @Autowired MongoTemplate template;

  public Optional<Auction> placeBid(String auctionId, String userId, BigDecimal bidValue) {
    Instant now = Instant.now();
    Query q = new Query().addCriteria(Criteria.where("_id").is(auctionId)).addCriteria(Criteria.where("status").is("ACTIVE")).addCriteria(Criteria.where("endTime").gt(now)).addCriteria(Criteria.where("highestBid").lt(bidValue));

    Update u = new Update().set("highestBid", bidValue).set("highestBidderId", userId).inc("bidsCount", 1);

    FindAndModifyOptions OPTS = new FindAndModifyOptions().returnNew(true);
    Auction updated = template.findAndModify(q, u, OPTS, Auction.class);
    return Optional.ofNullable(updated);
  }

  public Optional<Auction> endAuction(String auctionId) {
    Query q = new Query().addCriteria(Criteria.where("_id").is(auctionId)).addCriteria(Criteria.where("status").is("ACTIVE"));
    Update u = new Update().set("status", "ENDED").set("endTime", Instant.now());
    FindAndModifyOptions OPTS = new FindAndModifyOptions().returnNew(true);
    Auction updated = template.findAndModify(q, u, OPTS, Auction.class);
    return Optional.ofNullable(updated);
  }
}
