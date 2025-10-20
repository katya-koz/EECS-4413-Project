package com.bluebid.catalogue_app_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bluebid.catalogue_app_service.model.CatalogueItem;
import com.bluebid.catalogue_app_service.repository.CatalogueRepository;

@ExtendWith(MockitoExtension.class)
public class CatalogueServiceTest {

    @Mock
    private CatalogueRepository _catalogueRepository; // mock the repository

    @InjectMocks
    private CatalogueService _catalogueService; // real service we are testing

    private CatalogueItem cat0, cat1, cat2;

    @BeforeEach
    void setup() {
        cat0 = new CatalogueItem("cat0", "A", LocalDateTime.now().plusDays(10));
        cat1 = new CatalogueItem("cat1", "B", LocalDateTime.now().plusDays(2));
        cat2 = new CatalogueItem("cat2", "C", LocalDateTime.now().minusDays(3));
    }

    @Test
    void getAllItems_shouldReturnAllAvailableItems() {
        // Only items with auctionEndDate > now should be returned
        when(_catalogueRepository.findAll()).thenReturn(List.of(cat0, cat1, cat2));

        List<CatalogueItem> result = _catalogueService.getAllAvailableItems(0);

        // check only active items
        assertEquals(2, result.size());
        assertEquals("cat0", result.get(0).getItemID());
        assertEquals("cat1", result.get(1).getItemID());
    }

    @Test
    void searchItems_shouldReturnAvailableMatchingItems() {
        String keyword = "DVD";

        CatalogueItem dvd0 = new CatalogueItem("cat0","Click! DVD", LocalDateTime.now().plusDays(10));
        CatalogueItem dvd1 = new CatalogueItem("cat1","50 First Dates DVD", LocalDateTime.now().plusDays(2));
        CatalogueItem dvd2 = new CatalogueItem("cat2","The Devil Wears Prada DVD", LocalDateTime.now().minusDays(3));

        when(_catalogueRepository.findByItemNameContainingAndAuctionEndDateAfter(
                eq(keyword), any(LocalDateTime.class)
        )).thenReturn(List.of(dvd0, dvd1)); // only active items

        List<CatalogueItem> result = _catalogueService.searchAvailableItems(keyword, 0);

        assertEquals(2, result.size());
        assertEquals("cat0", result.get(0).getItemID());
        assertEquals("cat1", result.get(1).getItemID());
    }
}
