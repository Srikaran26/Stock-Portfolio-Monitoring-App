package com.example.portfolio.scheduler;

import com.example.portfolio.repository.StockPriceCacheRepository;
import com.example.portfolio.service.StockPriceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PriceCacheScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PriceCacheScheduler.class);

    private final StockPriceService stockPriceService;
    private final StockPriceCacheRepository cacheRepository;

    public PriceCacheScheduler(StockPriceService stockPriceService, StockPriceCacheRepository cacheRepository) {
        this.stockPriceService = stockPriceService;
        this.cacheRepository = cacheRepository;
    }

    @Scheduled(fixedRateString = "${cache.refresh.interval.ms:600000}")
    public void refreshCache() {
        logger.info("Scheduler started: refreshing cached stock prices");

        List<String> symbolsToUpdate = getTrackedSymbols();

        for (String symbol : symbolsToUpdate) {
            try {
                double price = stockPriceService.getPrice(symbol);
                logger.info("Updated price for {}: {}", symbol, price);
            } catch (Exception e) {
                logger.error("Error updating price for symbol: {}", symbol, e);
            }
        }

        logger.info("Scheduler finished refreshing cache");
    }

    private List<String> getTrackedSymbols() {
        return cacheRepository.findAll()
                .stream()
                .map(stockPriceCache -> stockPriceCache.getStockSymbol())
                .toList();
    }
}
