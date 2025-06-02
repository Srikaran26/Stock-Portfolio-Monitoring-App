package com.portfolio.service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.portfolio.exception.StockPriceFetchException;
import com.portfolio.model.StockPriceCache;
import com.portfolio.repository.StockPriceCacheRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

@Service
public class StockPriceServiceImpl implements StockPriceService {

    private static final Logger logger = LoggerFactory.getLogger(StockPriceServiceImpl.class);

    private final StockPriceCacheRepository cacheRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${rapidapi.key}")
    private String rapidApiKey;

    @Value("${rapidapi.host}")
    private String rapidApiHost;

    @Value("${cache.validity.hours:24}")
    private int cacheValidityHours;

    private Map<String, StockPriceCache> fakeCacheMap;
    

    public StockPriceServiceImpl(StockPriceCacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    private Double fetchPriceFromRapidApi(String symbol) {
        String url = "https://" + rapidApiHost + "/v1/rapidapi/stock/quote?tradingSymbol=" + symbol + "&exchange=NSE";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", rapidApiKey);
        headers.set("X-RapidAPI-Host", rapidApiHost);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String body = response.getBody();
            logger.info("📡 Raw API response for {}: {}", symbol, body);

            if (body == null || body.isEmpty()) return null;

            JSONObject json = new JSONObject(body);
            return json.has("lastPrice") ? json.getDouble("lastPrice") : null;
        } catch (Exception e) {
            logger.error("Error calling RapidAPI for {}: {}", symbol, e.getMessage());
            return null;
        }
    }
    @Override
    public double getPrice(String symbol) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Double> future = executor.submit(() -> fetchPriceFromRapidApi(symbol));

        Double price = null;

        try {
            price = future.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException te) {
            logger.warn("API timeout for symbol {}", symbol);
            future.cancel(true);
        } catch (Exception e) {
            logger.error("API exception for {}: {}", symbol, e.getMessage());
        } finally {
            executor.shutdown();
        }

        if (price != null) {
            StockPriceCache cache = new StockPriceCache();
            cache.setStockSymbol(symbol);
            cache.setPrice(price);
            cache.setLastUpdated(LocalDateTime.now());
            cacheRepository.save(cache);
            logger.info("✅ Updated price from API for {}: {}", symbol, price);
            return price;
        }

        Optional<StockPriceCache> optionalCache = cacheRepository.findById(symbol);
        if (optionalCache.isPresent()) {
            StockPriceCache cached = optionalCache.get();
            logger.warn("Using cached price for {} (may be outdated): {}", symbol, cached.getPrice());
            return cached.getPrice(); 
        }

        logger.error("No cached price found for {}", symbol);
        throw new StockPriceFetchException("Unable to fetch stock price for: " + symbol);
    }

    @Override
    public double refreshPrice(String symbol) {
        Double price = fetchPriceFromRapidApi(symbol);
        if (price == null) {
            throw new StockPriceFetchException("Failed to refresh price for: " + symbol);
        }

        StockPriceCache cache = new StockPriceCache();
        cache.setStockSymbol(symbol);
        cache.setPrice(price);
        cache.setLastUpdated(LocalDateTime.now());
        cacheRepository.save(cache);

        return price;
    }

    @Override
    public List<StockPriceCache> getAllCachedPrices() {
        return cacheRepository.findAll();
    }

    @Override
    public void clearCache() {
        cacheRepository.deleteAll();
        logger.info("vCache cleared");
    }

    @Override
    public boolean isApiAvailable() {
        try {
            return fetchPriceFromRapidApi("TATAMOTORS") != null;
        } catch (Exception e) {
            logger.error("API availability check failed", e);
            return false;
        }
    }

    @Override
    public int getCacheSize() {
        return (int) cacheRepository.count();
    }

    public Map<String, StockPriceCache> getFakeCacheMap() {
        return fakeCacheMap;
    }

    public void setFakeCacheMap(Map<String, StockPriceCache> fakeCacheMap) {
        this.fakeCacheMap = fakeCacheMap;
    }
}