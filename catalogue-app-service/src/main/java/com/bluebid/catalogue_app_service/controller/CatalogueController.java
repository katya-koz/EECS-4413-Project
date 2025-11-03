package com.bluebid.catalogue_app_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bluebid.catalogue_app_service.dto.PostNewItemRequest;
import com.bluebid.catalogue_app_service.model.CatalogueItem;
import com.bluebid.catalogue_app_service.service.CatalogueService;

@RestController
@RequestMapping("/catalogue")
public class CatalogueController {
	private final CatalogueService _catalogueService;
	private final KafkaTemplate<String, Object> _kafkaTemplate;
	public CatalogueController(CatalogueService catalogueService, KafkaTemplate<String, Object> _kafkaTemplate) {
	    this._catalogueService = catalogueService;
	    this._kafkaTemplate = _kafkaTemplate;
	}
	
	@GetMapping("/items")
	public ResponseEntity<List<CatalogueItem>> getCatalogue(@RequestParam(required = false) String keyword, @RequestParam int page) {
	    List<CatalogueItem> catalogue;
		if (keyword != null && !keyword.isBlank()) {
	        catalogue = _catalogueService.searchAvailableItems(keyword, page);
	    } else {
	        // get all catalogue items
	    	System.out.println("getting all items");
	    	catalogue = _catalogueService.getAllAvailableItems(page);
	    }
	
	    return ResponseEntity.ok(catalogue);
	}
	
//	@PostMapping("/item")
//	public ResponseEntity<Map<String,String>> postCatalogueItem(@RequestBody PostNewItemRequest newItemRequest){
//		String newCatalogueId = _catalogueService.uploadItem(newItemRequest);
//		System.out.println("finished upload " + newCatalogueId);
//		Map<String, String> responseBody = Map.of(
//		        "message", "Item created successfully!",
//		        "catalogueId", newCatalogueId);	
//		
//		
//		return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
//	}

}
