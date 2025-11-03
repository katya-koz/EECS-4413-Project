package com.bluebid.catalogue_app_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//import com.bluebid.auction_app_service.dto.InitiateAuctionEvent;
//import com.bluebid.auction_app_service.model.Auction;
//import com.bluebid.catalogue_app_service.dto.AuctionUploadEvent;
import com.bluebid.catalogue_app_service.dto.BidInitiatedEvent;
import com.bluebid.catalogue_app_service.dto.ItemAddedFailureEvent;
import com.bluebid.catalogue_app_service.dto.ItemAddedSuccessEvent;
import com.bluebid.catalogue_app_service.dto.ItemValidationFailureEvent;
import com.bluebid.catalogue_app_service.dto.ItemValidationSuccessEvent;
import com.bluebid.catalogue_app_service.dto.PaymentInitiatedEvent;
import com.bluebid.catalogue_app_service.dto.UploadCatalogueItemEvent;
import com.bluebid.catalogue_app_service.model.CatalogueItem;
import com.bluebid.catalogue_app_service.repository.CatalogueRepository;
//import com.bluebid.payment_app_service.model.Receipt;

@Service
public class CatalogueService {
	
	private final CatalogueRepository _catalogueRepository;
	private final KafkaTemplate<String, Object> _kafkaTemplate;
	private static final int MAX_CHAR_LIMIT = 100;

	public CatalogueService(CatalogueRepository catalogueRepository, KafkaTemplate<String, Object> kafkaTemplate) {
		this._kafkaTemplate = kafkaTemplate;
        this._catalogueRepository = catalogueRepository;
    }
	
	public List<CatalogueItem> getAllAvailableItems(int page) {
        return _catalogueRepository.findByIsActive(true);
    }

    public List<CatalogueItem> searchAvailableItems(String keyword, int page) {
    	// we only want items that are still up for auction
    	
        return _catalogueRepository.findByKeywordQuery(keyword ); // case insensitive search
        
    }
    
    //############################## PAY FOR ITEM USE CASE ###########################
    @KafkaListener(topics= "payment.item-validation-topic", groupId = "catalogue-group", containerFactory = "paymentInitiatedListenerContainerFactory")
    public void reserveItemForPurchase(PaymentInitiatedEvent event) {
    	// once a payment event has been received for an item, and the user is validated, it must be set to inactive in the db, and information for the receipt must be fetched and published	
    	
    	String itemId = event.getCatalogueID();
    	Optional<CatalogueItem> optionalItem = _catalogueRepository.findByIdAndIsActive(itemId, true);
        if (optionalItem.isPresent()) {
            CatalogueItem item = optionalItem.get();
            String itemName = item.getItemName();
            int shipping = event.getIsExpedited() ? item.getExpeditedShippingDays() : item.getShippingDays();
            Double shippingCost = event.getIsExpedited() ? item.getExpeditedShippingCost() : item.getShippingCost();
            Double itemCost = item.getCurrentBiddingPrice();
            String highestBidderUserId = item.getHighestBidderID();
            LocalDateTime auctionEnd = item.getAuctionEndTime();
            
            
            if(event.getPaymentTime().isBefore(auctionEnd)) {
            	_kafkaTemplate.send("catalogue.payment-item-validation-failed-topic", new ItemValidationFailureEvent(event.getCatalogueID(),event.getId(),"This auction is not yet over." ));

            }else if(event.getUserID().equals(event.getSellerID())) {
            	_kafkaTemplate.send("catalogue.payment-item-validation-failed-topic", new ItemValidationFailureEvent(event.getCatalogueID(),event.getId(), "A seller cannot purchase their own listing!." ));

            }else if(!event.getUserID().equals(highestBidderUserId)) {
            	// return a fail if the user who is attempting to purchase the item is not the one who won the bid
            	_kafkaTemplate.send("catalogue.payment-item-validation-failed-topic", new ItemValidationFailureEvent(event.getCatalogueID(),event.getId(), "Nice try, punk... that requested user ID is not the same as the auction winner." ));
            }
            
            else {
            
	            if (Math.abs(itemCost - event.getExpectedItemCost()) < 0.0001 && Math.abs(shippingCost - event.getExpectedShippingCost()) < 0.0001) { // tolerance for double math
	                _kafkaTemplate.send("catalogue.payment-item-validation-success-topic", new ItemValidationSuccessEvent(itemId, itemName, shipping, shippingCost, itemCost, event.getId()));
	                item.setIsActive(false); // set item to be inactive
	                _catalogueRepository.save(item);
	            }else {
	            	_kafkaTemplate.send("catalogue.payment-item-validation-failed-topic", new ItemValidationFailureEvent(event.getCatalogueID(),event.getId(), "The item and/or shipping cost in the backend is different than what the payment was initiated for."));
	            }  }
        }else {
        	// keep choreography events from hanging. publish failed response.
        	_kafkaTemplate.send("catalogue.payment-item-validation-failed-topic", new ItemValidationFailureEvent(event.getCatalogueID(),event.getId(),"This item was not found to be active in our database." ));
        }
    	
    }
    
    
  //############################## BID ON ITEM USE CASE ###########################
    @KafkaListener(topics= "bid.item-validation-topic", groupId = "catalogue-group", containerFactory = "bidInitiatedListenerContainerFactory")
    public void validateItemBidAmount(BidInitiatedEvent event) {
    	// once a payment event has been received for an item, and the user is validated, it must be set to inactive in the db, and information for the receipt must be fetched and published	
    	
    	String itemId = event.getCatalogueID();
    	Optional<CatalogueItem> optionalItem = _catalogueRepository.findByIdAndIsActive(itemId, true);
        if (optionalItem.isPresent()) {
            CatalogueItem item = optionalItem.get();
            String itemName = item.getItemName();
            
            // not checking for highest bidder id. users are allowed to bid for the same item multiple times in a row :)
            
  
            if (item.getCurrentBiddingPrice() < event.getBidAmount()) { 
                _kafkaTemplate.send("catalogue.bid-item-validation-success-topic", new ItemValidationSuccessEvent(itemId, itemName,-1,-1.0, -1.0, event.getId()));
                item.setCurrentBiddingPrice(event.getBidAmount());
                item.setHighestBidderID(event.getUserID());
                _catalogueRepository.save(item);
            }else {
            	_kafkaTemplate.send("catalogue.bid-item-validation-failed-topic", new ItemValidationFailureEvent(event.getCatalogueID(),event.getId(), "The current bidding cost is different than what the bid request was initiated for."));
            }
        }else {
        	// keep choreography events from hanging. publish failed response.
        	_kafkaTemplate.send("catalogue.bid-item-validation-failed-topic", new ItemValidationFailureEvent(event.getCatalogueID(),event.getId(),"This item was not found to be active in our database." ));
        }
    	
    }
  //############################## NEW AUCTION USE CASE ###########################
    @KafkaListener(topics = "auction.validation-topic", groupId = "catalogue-group", containerFactory = "auctionInitiatedListenerContainerFactory")
    public String addToCatalogue(UploadCatalogueItemEvent event)
  	{
		String itemName= event.getItemName();
		String itemDescription= event.getItemDescription();
		String sellerID= event.getSellerID();
		double basePrice= event.getBasePrice();
    	
  		//InitiateAuctionEvent event = new InitiateAuctionEvent(itemName, itemDescription, sellerID, isValid, basePrice, days, hours);
  		boolean areItemsValid = validateCatalogueInput(itemName, itemDescription, sellerID, basePrice);
  		
  		
  		//create Catalogue Item
  		CatalogueItem item = new CatalogueItem();
  		item.setSellerID(event.getSellerID());
		item.setItemName(event.getItemName());
		item.setAuctionStartDate(event.getAuctionStartDate());
		item.setAuctionEndTime(event.getAuctionEndDate());
		item.setItemDescription(event.getItemDescription());  	
		
		//generate random shipping costs and times
		
	    ThreadLocalRandom random = ThreadLocalRandom.current();

	    double shippingCost = random.nextDouble(5.0, 20.0);
	    double expeditedShippingCost = shippingCost * random.nextDouble(1.2, 1.8);
	    int shippingDays = random.nextInt(3, 11);
	    int expeditedShippingDays = Math.max(1, shippingDays - random.nextInt(1, 4));

	    item.setShippingCost(shippingCost);
	    item.setExpeditedShippingCost(expeditedShippingCost);
	    item.setShippingDays(shippingDays);
	    item.setExpeditedShippingDays(expeditedShippingDays);
	    
	    
	    item.setHighestBidderID(null);
	    item.setIsActive(true);
	    item.setCurrentBiddingPrice(basePrice); 
  				
  		//if validationation passes here, publish a success event here
  		if (areItemsValid)
  		{
  			
  			//save the catalogue item into the db
  	  		_catalogueRepository.save(item);
  	  		_kafkaTemplate.send("catalogue.item-add-success-topic", new ItemAddedSuccessEvent(item.getId(),item.getItemName(),item.getItemDescription(),item.getSellerID(),event.getAuctionId()));
  	  	

  		}
  		//if validation fails, publish a failure event here
  		else
  		{
  			_kafkaTemplate.send("catalogue.item-add-failure-topic", new ItemAddedFailureEvent(item.getId(),item.getItemName(),item.getItemDescription(),item.getSellerID(), "Validation Failed. Item not added to the database.",event.getAuctionId()));
  		}
  		  		
  		
  		return item.getId();
 
  	}
    
    
  //logical validation methods
  	private boolean validateCatalogueInput(String itemName, String itemDescription, String sellerID, double basePrice)
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
  	

    
  
}
