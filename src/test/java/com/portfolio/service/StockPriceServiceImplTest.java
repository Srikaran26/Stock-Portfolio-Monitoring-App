package com.portfolio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.portfolio.exception.StockPriceFetchException;
import com.portfolio.model.StockPriceCache;
import com.portfolio.service.StockPriceServiceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class StockPriceServiceImplTest {

    private StockPriceServiceImpl stockPriceService;

    @BeforeEach
    public void setup() {
        stockPriceService = new StockPriceServiceImpl(null);

        // Setting up fake cache
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
        cache.setLastUpdated(LocalDateTime.now().minusHours(2)); 

        stockPriceService.getFakeCacheMap().put("TATAMOTORS", cache);

        assertThrows(StockPriceFetchException.class, () -> {
            stockPriceService.getPrice("TATAMOTORS");
        }, "Should throw exception when cache is expired");
    }

    @Test
    public void testThrowsExceptionWhenNoCacheExists() {

        assertThrows(StockPriceFetchException.class, () -> {
            stockPriceService.getPrice("TATAMOTORS");
        }, "Should throw exception when no cache exists");
    }
}
