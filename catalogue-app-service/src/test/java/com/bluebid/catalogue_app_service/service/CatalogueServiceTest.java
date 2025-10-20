package com.bluebid.catalogue_app_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
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

    private CatalogueItem cat0, cat1, cat2, cat3, cat4;

    @BeforeEach
    void setup() {
        cat0 = new CatalogueItem("cat0", "Click! DVD", LocalDateTime.now().plusDays(10));
        cat1 = new CatalogueItem("cat1", "50 First Dates DVD", LocalDateTime.now().plusDays(2));
        cat2 = new CatalogueItem("cat2", "The Devil Wears Prada DVD", LocalDateTime.now().minusDays(3));
        cat3 = new CatalogueItem("cat3", "A shirt", LocalDateTime.now().plusDays(1));
        cat4 = new CatalogueItem("cat4", "A pair of pants", LocalDateTime.now().plusDays(4));
    }

    @Test
    void getAllItems_shouldReturnAllAvailableItems() {

        when(_catalogueRepository.findByAuctionEndDateAfter(any())).thenReturn(List.of(cat0, cat1));


        List<CatalogueItem> result = _catalogueService.getAllAvailableItems(0);

        // check only active items
        assertEquals(4, result.size());
        assertEquals("cat0", result.get(0).getItemID());
        assertEquals("cat1", result.get(1).getItemID());
        assertEquals("cat3", result.get(2).getItemID());
        assertEquals("cat4", result.get(3).getItemID());
        
        
        verify(_catalogueRepository).findByAuctionEndDateAfter(any());
        
       
    }

    @Test
    void searchItems_shouldReturnAvailableMatchingItems() {
        String keyword = "DVD";


        when(_catalogueRepository.findByItemNameContainingAndAuctionEndDateAfter(eq(keyword), any())).thenReturn(List.of(cat0, cat1)); // only active items with dvd in title

        List<CatalogueItem> result = _catalogueService.searchAvailableItems(keyword, 0);

        assertEquals(2, result.size());
        assertEquals("cat0", result.get(0).getItemID());
        assertEquals("cat1", result.get(1).getItemID());
        
        // verify mock
        verify(_catalogueRepository).findByItemNameContainingAndAuctionEndDateAfter(eq(keyword), any());
    }
}
