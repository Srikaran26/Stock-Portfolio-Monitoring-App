package com.example.portfolio.service;

import com.example.portfolio.exception.StockPriceFetchException;
import com.example.portfolio.model.StockPriceCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class StockPriceServiceImplTest {

    private StockPriceServiceImpl stockPriceService;

    @BeforeEach
    public void setup() {
        // Initialize the service with null repository since we are only testing fake cache logic
        stockPriceService = new StockPriceServiceImpl(null);

        // Setup fake cache
        stockPriceService.setFakeCacheMap(new HashMap<>());
    }

    @Test
    public void testReturnsCachedValueWhenCacheIsFresh() {
        StockPriceCache cache = new StockPriceCache();
        cache.setStockSymbol("TATAMOTORS");
        cache.setPrice(150.0);
        cache.setLastUpdated(LocalDateTime.now().minusMinutes(10)); // Fresh cache

        stockPriceService.getFakeCacheMap().put("TATAMOTORS", cache);

        double price = stockPriceService.getPrice("TATAMOTORS");

        assertEquals(150.0, price, "Price should be returned from fresh cache");
    }

    @Test
    public void testThrowsExceptionWhenCacheExpired() {
        StockPriceCache cache = new StockPriceCache();
        cache.setStockSymbol("TATAMOTORS");
        cache.setPrice(100.0);
        cache.setLastUpdated(LocalDateTime.now().minusHours(2)); // Expired cache

        stockPriceService.getFakeCacheMap().put("TATAMOTORS", cache);

        assertThrows(StockPriceFetchException.class, () -> {
            stockPriceService.getPrice("TATAMOTORS");
        }, "Should throw exception when cache is expired");
    }

    @Test
    public void testThrowsExceptionWhenNoCacheExists() {
        // No entry added to cache

        assertThrows(StockPriceFetchException.class, () -> {
            stockPriceService.getPrice("TATAMOTORS");
        }, "Should throw exception when no cache exists");
    }
}
