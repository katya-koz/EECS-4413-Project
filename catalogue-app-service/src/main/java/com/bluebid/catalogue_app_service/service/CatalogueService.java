package com.bluebid.catalogue_app_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.bluebid.catalogue_app_service.dto.CatalogueItemReadyEvent;
import com.bluebid.catalogue_app_service.dto.ItemReserveFailedEvent;
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
    
    @KafkaListener(topics= "payment.payment-initiated-topic", groupId = "catalogue-group", containerFactory = "paymentInitiatedListenerContainerFactory")
    public void reserveItemForPurchase(PaymentInitiatedEvent event) {
    	// once a payment event has been received for an item, it must be set to inactive in the db, and information for the receipt must be fetched and published	
    	
    	
    	String itemId = event.getCatalogueID();
    	Optional<CatalogueItem> optionalItem = _catalogueRepository.findByIdAndIsActive(itemId, true);
        if (optionalItem.isPresent()) {
            CatalogueItem item = optionalItem.get();
            item.setIsActive(false); // set item to be inactive
            _catalogueRepository.save(item);
            
            String itemName = item.getItemName();
            int shipping = event.getIsExpedited() ? item.getExpeditedShippingDays() : item.getShippingDays();
            Double shippingCost = event.getIsExpedited() ? item.getExpeditedShippingCost() : item.getShippingCost();
            Double itemCost = item.getCurrentBiddingPrice();
            
            if (Math.abs(itemCost - event.getExpectedItemCost()) < 0.0001 && Math.abs(shippingCost - event.getExpectedShippingCost()) < 0.0001) { // tolerance for double math
                _kafkaTemplate.send("catalogue.catalogue-topic", new CatalogueItemReadyEvent(itemId, itemName, shipping, shippingCost, itemCost, event.getId()));
            }else {
            	_kafkaTemplate.send("catalogue.item-failed-topic", new ItemReserveFailedEvent("The item and/or shipping cost in the backend is different than what the payment was initiated for.", event.getId()));
            }
        }else {
        	// keep choreography events from hanging. publish failed response.
        	_kafkaTemplate.send("catalogue.item-failed-topic", new ItemReserveFailedEvent("This item was not found to be active in our database.", event.getId()));
        }
    	
    }
}
