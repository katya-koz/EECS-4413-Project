//package com.bluebid.auction_app_service.service;
//
//import com.bluebid.auction_app_service.dto.AuctionNotification;
//import com.bluebid.auction_app_service.model.Auction;
//import com.bluebid.auction_app_service.model.Bid;
//import com.bluebid.auction_app_service.repository.AuctionRepository;
//import com.bluebid.auction_app_service.repository.BidRepository;
//
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class AuctionServiceTest {
//
//	@Mock
//	private AuctionRepository _auctionRepository;
//	
//	@Mock
//	private BidRepository _bidRepository;
//	
//	@Mock
//	private SimpMessageSendingOperations _messagingTemplate;
//	
//	@InjectMocks
//	private AuctionService _auctionService;
//	@Test
//	void placing_Higher_Bid_Should_Return_Success() {
//	    String auctionId = "A1";
//		String userId = "U1";
//		Double newBidValue = 200.0;
//		
//		Auction auction = new Auction();
//		auction.setBasePrice(100.0);
//		
//		when(_bidRepository.findByAuctionIDOrderByAmountDesc(auctionId)).thenReturn(List.of());
//		when(_auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
//		
//		String result = _auctionService.placeBid(auctionId, userId, newBidValue);
//		
//		assertTrue(result.contains("successfully"));
//		verify(_bidRepository, times(1)).save(any(Bid.class));
//	}
//	@Test
//	void placing_Lower_Bid_Should_Not_Save() {
//	    String auctionId = "A1";
//		String userId = "U1";
//		
//		Bid highestBid = new Bid();
//		highestBid.setAmount(300.0);
//		
//		when(_bidRepository.findByAuctionIDOrderByAmountDesc(auctionId))
//		        .thenReturn(List.of(highestBid));
//		
//		String result = _auctionService.placeBid(auctionId, userId, 200.0);
//		
//		assertTrue(result.contains("Bid value too low"));
//		
//		// check that the bid was never saved (bid repo method was never called)
//		verify(_bidRepository, never()).save(any(Bid.class));
//	}
//	@Test
//	void ending_Auction_Before_EndTime_Should_Not_Notify() {
//	    String auctionId = "A1";
//		
//		Auction auction = new Auction();
//		auction.setAuctionEndTime(LocalDateTime.now().plusMinutes(10)); // not ended yet
//		
//		when(_auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
//		
//		_auctionService.endAuction(auctionId);
//		
//		verify(_auctionRepository, never()).save(any(Auction.class));
//		verify(_messagingTemplate, never()).convertAndSend(anyString(), any(AuctionNotification.class));
//	}
//	
//	@Test
//	void ending_Auction_After_EndTime_Should_Notify() {
//	    String auctionId = "A1";
//	
//		Auction auction = new Auction();
//		auction.setAuctionEndTime(LocalDateTime.now().minusMinutes(5)); // ended
//		
//		Bid winnerBid = new Bid();
//		winnerBid.setBidderID("U1");
//		winnerBid.setAmount(500.0);
//		
//		when(_auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
//		when(_bidRepository.findByAuctionIDOrderByAmountDesc(auctionId)).thenReturn(List.of(winnerBid));
//		
//		_auctionService.endAuction(auctionId);
//		
//		verify(_auctionRepository, times(1)).save(any(Auction.class));
//		verify(_messagingTemplate, times(1))
//		        .convertAndSend(eq("/topic/auction/" + auctionId), any(AuctionNotification.class));
//	}
//	@Test
//	void ending_Auction_With_No_Bids_Should_Do_Nothing() {
//		String auctionId = "A1";
//		
//		Auction auction = new Auction();
//		auction.setAuctionEndTime(LocalDateTime.now().minusMinutes(5)); // ended
//		
//		when(_auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
//		when(_bidRepository.findByAuctionIDOrderByAmountDesc(auctionId)).thenReturn(List.of());
//		
//		_auctionService.endAuction(auctionId);
//		
//		verify(_messagingTemplate, never()).convertAndSend(anyString(), any(AuctionNotification.class));
//	}
//}
