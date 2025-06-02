package com.portfolio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.portfolio.model.StockPriceCache;
import com.portfolio.service.StockPriceService;

import java.util.List;

@RestController
@RequestMapping("/api/stock-prices")
public class StockPriceController {

    private final StockPriceService stockPriceService;

    public StockPriceController(StockPriceService stockPriceService) {
        this.stockPriceService = stockPriceService;
    }

    @GetMapping("/{symbol}")
    public double getPrice(@PathVariable String symbol) {
        return stockPriceService.getPrice(symbol);
    }

    @GetMapping("/cache")
    public List<StockPriceCache> getAllCachedPrices() {
        return stockPriceService.getAllCachedPrices();
    }

    @DeleteMapping("/cache")
    public ResponseEntity<String> clearCache() {
        stockPriceService.clearCache();
        return ResponseEntity.ok("Cache cleared successfully");
    }

    @GetMapping("/{symbol}/refresh")
    public double refreshPrice(@PathVariable String symbol) {
        return stockPriceService.refreshPrice(symbol);
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        boolean apiAvailable = stockPriceService.isApiAvailable();
        int cacheSize = stockPriceService.getCacheSize();
        String status = "API Available: " + apiAvailable + ", Cache Size: " + cacheSize;
        return ResponseEntity.ok(status);
    }
}
