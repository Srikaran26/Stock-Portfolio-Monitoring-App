package com.portfolio.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.portfolio.repository.StockPriceCacheRepository;
import com.portfolio.service.StockPriceService;

import java.util.List;

@Service
public class PriceCacheScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PriceCacheScheduler.class);

    private final StockPriceService stockPriceService;
    private final StockPriceCacheRepository cacheRepository;

    public PriceCacheScheduler(StockPriceService stockPriceService,
                                StockPriceCacheRepository cacheRepository) {
        this.stockPriceService = stockPriceService;
        this.cacheRepository = cacheRepository;
    }

    @Scheduled(fixedRateString = "${cache.refresh.interval.ms:600000}")
    public void refreshCache() {
        logger.info("Scheduler triggered: Starting stock price cache refresh");

        List<String> symbolsToUpdate = getTrackedSymbols();

        for (String symbol : symbolsToUpdate) {
            try {
                double price = stockPriceService.refreshPrice(symbol);  // Fetch from API
                logger.info("Refreshed price for [{}]: {}", symbol, price);
            } catch (Exception e) {
                logger.warn("Skipped [{}] due to API failure: {}", symbol, e.getMessage());
            }
        }

        logger.info("✅ Scheduler completed cache refresh cycle");
    }

    
    private List<String> getTrackedSymbols() {
        return cacheRepository.findAll()
                .stream()
                .map(stock -> stock.getStockSymbol())
                .distinct()
                .toList();
    }
}
