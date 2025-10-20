package com.bluebid.catalogue_app_service.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bluebid.catalogue_app_service.model.CatalogueItem;

public interface CatalogueRepository extends MongoRepository<CatalogueItem, String> {
	
	// custom query
	List<CatalogueItem> findByItemNameContainingAndAuctionEndDateAfter(String keyword, LocalDateTime now);

	List<CatalogueItem> findByAuctionEndDateAfter(LocalDateTime now);
}