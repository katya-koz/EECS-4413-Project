package com.bluebid.catalogue_app_service.service;

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
	
	public List<CatalogueItem> getAllItems(int page) {
		// implement page...
        return _catalogueRepository.findAll();
    }

    public List<CatalogueItem> searchItems(String keyword, int page) {
    	// implement page...
        return _catalogueRepository.findByItemNameContaining(keyword);
    }
    
}
