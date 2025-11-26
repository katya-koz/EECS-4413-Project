package com.bluebid.catalogue_app_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	// return page (search)
	@Query("{ 'itemName': { $regex: ?0, $options: 'i' }, 'auctionEndDate': { $gt: ?1 } }")
    Page<CatalogueItem> findByKeywordQueryAndNotEnded(String keyword, LocalDateTime now, Pageable pageable);
	
	// return page (no search)
	 Page<CatalogueItem> findByAuctionEndDateAfter(LocalDateTime now, Pageable pageable);

	// find by id (isactive means that the item has not been bought yet.)
	Optional<CatalogueItem> findByIdAndIsActive(String catalogueID, boolean b);
	
}