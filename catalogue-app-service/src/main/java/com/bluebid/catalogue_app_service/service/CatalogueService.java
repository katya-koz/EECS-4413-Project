package com.bluebid.catalogue_app_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bluebid.catalogue_app_service.model.CatalogueItem;
import com.bluebid.catalogue_app_service.repository.CatalogueRepository;

@Service
public class CatalogueService {
	
	private final CatalogueRepository _catalogueRepository;
	
	public CatalogueService(CatalogueRepository catalogueRepository) {
        this._catalogueRepository = catalogueRepository;
    }
	
	public List<CatalogueItem> getAllAvailableItems(int page) {
        return _catalogueRepository.findByAuctionEndDateAfter(LocalDateTime.now());
    }

    public List<CatalogueItem> searchAvailableItems(String keyword, int page) {
    	// we only want items that are still up for auction
    	
        return _catalogueRepository.findByItemNameContainingAndAuctionEndDateAfter(keyword, LocalDateTime.now());
    }
    
}
