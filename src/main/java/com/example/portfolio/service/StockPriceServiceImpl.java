package com.example.portfolio.service;

import com.example.portfolio.exception.StockPriceFetchException;
import com.example.portfolio.model.StockPriceCache;
import com.example.portfolio.repository.StockPriceCacheRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StockPriceServiceImpl implements StockPriceService {

    private static final Logger logger = LoggerFactory.getLogger(StockPriceServiceImpl.class);

    private final StockPriceCacheRepository cacheRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${rapidapi.key}")
    private String rapidApiKey;

    @Value("${rapidapi.host}")
    private String rapidApiHost;

    private WebClient rapidApiClient;

    public StockPriceServiceImpl(StockPriceCacheRepository cacheRepository, WebClient.Builder webClientBuilder) {
        this.cacheRepository = cacheRepository;
        this.webClientBuilder = webClientBuilder;
    }

    @PostConstruct
    public void init() {
        this.rapidApiClient = webClientBuilder.baseUrl("https://" + rapidApiHost).build();
    }

    // Fetch live price using RapidAPI
    private Double fetchPriceFromRapidApi(String symbol) {
        try {
            String response = rapidApiClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/rapidapi/stock/quote")
                            .queryParam("tradingSymbol", symbol)
                            .queryParam("exchange", "NSE")
                            .build())
                    .header("X-RapidAPI-Key", rapidApiKey)
                    .header("X-RapidAPI-Host", rapidApiHost)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            logger.info("Raw API response for symbol {}: {}", symbol, response);

            if (response == null || response.isEmpty()) {
                logger.warn("Empty response from RapidAPI for symbol: {}", symbol);
                return null;
            }

            JSONObject json = new JSONObject(response);
            if (json.has("lastPrice")) {
                return json.getDouble("lastPrice");
            } else {
                logger.warn("lastPrice not found in RapidAPI response for symbol: {}", symbol);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error fetching price from RapidAPI for symbol: " + symbol, e);
            return null;
        }
    }

    @Override
    public double getPrice(String symbol) {
        Optional<StockPriceCache> cacheOptional = cacheRepository.findById(symbol);

        if (cacheOptional.isPresent()) {
            StockPriceCache cache = cacheOptional.get();
            if (cache.getLastUpdated().isAfter(LocalDateTime.now().minusHours(1))) {
                logger.info("Returning cached price for symbol: {}", symbol);
                return cache.getPrice();
            } else {
                logger.info("Cache expired for symbol: {}", symbol);
            }
        } else {
            logger.info("No cache found for symbol: {}", symbol);
        }

        Double livePrice = fetchPriceFromRapidApi(symbol);

        if (livePrice == null) {
            throw new StockPriceFetchException("Failed to fetch live price for symbol: " + symbol);
        }

        StockPriceCache newCache = new StockPriceCache();
        newCache.setStockSymbol(symbol);
        newCache.setPrice(livePrice);
        newCache.setLastUpdated(LocalDateTime.now());
        cacheRepository.save(newCache);

        return livePrice;
    }

    @Override
    public double refreshPrice(String symbol) {
        Double livePrice = fetchPriceFromRapidApi(symbol);

        if (livePrice == null) {
            throw new StockPriceFetchException("Failed to refresh live price for symbol: " + symbol);
        }

        StockPriceCache newCache = new StockPriceCache();
        newCache.setStockSymbol(symbol);
        newCache.setPrice(livePrice);
        newCache.setLastUpdated(LocalDateTime.now());
        cacheRepository.save(newCache);

        return livePrice;
    }

    @Override
    public List<StockPriceCache> getAllCachedPrices() {
        return cacheRepository.findAll();
    }

    @Override
    public void clearCache() {
        cacheRepository.deleteAll();
        logger.info("Cache cleared");
    }

    @Override
    public boolean isApiAvailable() {
        try {
            Double price = fetchPriceFromRapidApi("TATAMOTORS");
            return price != null;
        } catch (Exception e) {
            logger.error("API availability check failed", e);
            return false;
        }
    }

    @Override
    public int getCacheSize() {
        return (int) cacheRepository.count();
    }
}
