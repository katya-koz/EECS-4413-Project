package com.bluebid.catalogue_app_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.bluebid.catalogue_app_service.dto.BidInitiatedEvent;
import com.bluebid.catalogue_app_service.dto.ItemValidationFailureEvent;
import com.bluebid.catalogue_app_service.dto.ItemValidationSuccessEvent;
import com.bluebid.catalogue_app_service.dto.PaymentInitiatedEvent;
import com.bluebid.catalogue_app_service.model.CatalogueItem;
import com.bluebid.catalogue_app_service.repository.CatalogueRepository;

@Service
public class CatalogueService {
	
	private final CatalogueRepository _catalogueRepository;
	private final KafkaTemplate<String, Object> _kafkaTemplate;
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
            
            if(!event.getUserID().equals(highestBidderUserId)) {
            	// return a fail if the user who is attempting to purchase the item is not the one who won the bid
            	_kafkaTemplate.send("catalogue.payment-item-validation-failed-topic", new ItemValidationFailureEvent(event.getCatalogueID(),event.getId(), "Nice try, punk... that requested user ID is not the same as the auction winner." ));
            }else {
            
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
}
