package com.bluebid.catalogue_app_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
	
	public CatalogueController(CatalogueService catalogueService) {
	    this._catalogueService = catalogueService;
	}
	
	@GetMapping
	public ResponseEntity<List<CatalogueItem>> getCatalogue(@RequestParam(required = false) String keyword, @RequestParam int page) {
	
	   
	    List<CatalogueItem> catalogue;
		if (keyword != null && !keyword.isBlank()) {
	        catalogue = _catalogueService.searchItems(keyword, page);
	    } else {
	        // get all catalogue items
	    	catalogue = _catalogueService.getAllItems(page);
	    }
	
	    return ResponseEntity.ok(catalogue);
	}
	
	@PostMapping("/post-catalogue-item")
	public ResponseEntity<Boolean> postCatalogueItem(@RequestBody PostNewItemRequest newItemRequest){
		return ResponseEntity.ok(true);
		
	}

}
