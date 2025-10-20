package com.example.auction.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.auction.model.Auction;
import com.example.auction.model.BidRequest;
import com.example.auction.model.CreateAuctionRequest;
import com.example.auction.Repo.AuctionRepository;
import com.example.auction.service.AuctionService;
import com.example.auction.service.NotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auctions")
@Validated
public class AuctionController {

  @Autowired AuctionRepository Repo;
  @Autowired AuctionService auctionService;
  @Autowired NotificationService notificationService;

  @PostMapping
  public ResponseEntity<Auction> create(@Valid @RequestBody CreateAuctionRequest auction) {
    Auction a = new Auction(auction.getSellerId(), auction.getCatalogueId(), auction.getItemName(), auction.getBasePrice(), auction.getAuctionType(), auction.getShippingCost(), auction.getExpeditedShippingCost(), auction.getShippingDays(), auction.getEndTime());
    a = Repo.save(a);
    return ResponseEntity.created(URI.create("/api/auctions/" + a.getId())).body(a);
  }

  @GetMapping
  public List<Auction> list() { return Repo.findAll(); }

  @GetMapping("/{id}")
  public ResponseEntity<Auction> get(@PathVariable String id) {
    return Repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/{id}/bids")
  public ResponseEntity<?> bid(@PathVariable String id, @Valid @RequestBody BidRequest bid) {
    Optional<Auction> updated = auctionService.placeBid(id, bid.getUserId(), bid.getBidValue());
    if (updated.isPresent()) {
      return ResponseEntity.ok(updated.get());
    } else {
      return ResponseEntity.badRequest().body("Bid rejected: lower than current highest or auction not active");
    }
  }

  @PostMapping("/{id}/end")
  public ResponseEntity<?> end(@PathVariable String id) {
    Optional<Auction> ended = auctionService.endAuction(id);
    if (ended.isPresent()) {
      notificationService.sendAuctionEndNotification(id, ended.get().getHighestBidderId());
      return ResponseEntity.ok(ended.get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
