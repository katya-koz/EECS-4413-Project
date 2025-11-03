package com.bluebid.catalogue_app_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.bluebid.catalogue_app_service.model.CatalogueItem;

@Repository
public interface CatalogueRepository extends MongoRepository<CatalogueItem, String> {
	
	//List<CatalogueItem> findByItemNameContaining(String keyword);

	List<CatalogueItem> findByIsActive(boolean b);

	@Query("{ 'itemName': { $regex: ?0, $options: 'i' }, 'isActive': true }") // case insensitive
	List<CatalogueItem> findByKeywordQuery(String keyword);

	Optional<CatalogueItem> findByIdAndIsActive(String catalogueID, boolean b);
	
	// mark item as inactive once sold

	
	
	//get item info for reciept
}