package com.bluebid.catalogue_app_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bluebid.catalogue_app_service.model.CatalogueItem;
import com.bluebid.catalogue_app_service.service.CatalogueService;

@RestController
@RequestMapping("/catalogue")
public class CatalogueController {
	private final int PAGE_RESULTS_NUM = 36;
	private final CatalogueService _catalogueService;
	private final KafkaTemplate<String, Object> _kafkaTemplate;
	public CatalogueController(CatalogueService catalogueService, KafkaTemplate<String, Object> _kafkaTemplate) {
	    this._catalogueService = catalogueService;
	    this._kafkaTemplate = _kafkaTemplate;
	}
	
	@GetMapping("/items")
	public ResponseEntity<Page<CatalogueItem>> getCatalogue(@RequestParam(required = false) String keyword,  @RequestParam(defaultValue = "0") int page) {
		// request a page, not just a list
		PageRequest pageable = PageRequest.of(page, PAGE_RESULTS_NUM);
		Page<CatalogueItem> catalogue;
	    
		if (keyword != null && !keyword.isBlank()) {
	        catalogue = _catalogueService.searchAvailableItems(keyword, pageable);
	    } else {
	        // get all catalogue items
	    	
	    	catalogue = _catalogueService.getAllAvailableItems(pageable);
	    }
	
	    return ResponseEntity.ok(catalogue);
	}
	@GetMapping("/items/{itemId}")
	public ResponseEntity<?> getAuction(@PathVariable String itemId) {
	    Optional<CatalogueItem> item = _catalogueService.getItemById(itemId);
	    if (item != null) {
	        return ResponseEntity.ok(item);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}


}
