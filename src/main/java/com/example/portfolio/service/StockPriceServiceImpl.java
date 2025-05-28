package com.example.portfolio.service;

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
    public double getPrice(String stockSymbol) {
        Optional<StockPriceCache> cacheOptional = cacheRepository.findById(stockSymbol);

        if (cacheOptional.isPresent()) {
            StockPriceCache cache = cacheOptional.get();
            // Cache is valid for 1 hours so after 1 hour it can fetch the live prices
            if (cache.getLastUpdated().isAfter(LocalDateTime.now().minusHours(1))) {
                logger.info("Returning cached price for symbol: {}", stockSymbol);
                return cache.getPrice();
            } else {
                logger.info("Cache expired for symbol: {}", stockSymbol);
            }
        } else {
            logger.info("No cache found for symbol: {}", stockSymbol);
        }

        Double livePrice = fetchPriceFromRapidApi(stockSymbol);

        if (livePrice == null) {
            // If API call fails, return 0.0 (or you can throw exception)
            return 0.0;
        }

        StockPriceCache newCache = new StockPriceCache();
        newCache.setStockSymbol(stockSymbol);
        newCache.setPrice(livePrice);
        newCache.setLastUpdated(LocalDateTime.now());
        cacheRepository.save(newCache);

        return livePrice;
    }
}
