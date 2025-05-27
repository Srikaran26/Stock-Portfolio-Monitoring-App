package com.example.portfolio.service;

import com.example.portfolio.model.StockPriceCache;
import com.example.portfolio.repository.StockPriceCacheRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.time.LocalDateTime;
import java.util.Random;

@Service 
public class StockPriceServiceImpl {
	
	private final StockPriceCacheRepository cacheRepository;
	private final Random random = new Random();
	
	@Value("${twelvedata.api.key}")  //api key is added in properties fileeeee!
    private String apiKey;
	private final WebClient webClient;
	
	 public StockPriceServiceImpl(StockPriceCacheRepository cacheRepository, WebClient.Builder webClientBuilder) {
	        this.cacheRepository = cacheRepository;
	        this.webClient = webClientBuilder.baseUrl("https://api.twelvedata.com").build();
	    }

	    public double fetchLivePrice(String stockSymbol) {
	        try {
	            String symbolWithPrefix = "NSE:" + stockSymbol;  // Indian stocks require NSE prefix

	            String response = webClient.get()
	                    .uri(uriBuilder -> uriBuilder.path("/quote")
	                            .queryParam("symbol", symbolWithPrefix)
	                            .queryParam("apikey", apiKey)
	                            .build())
	                    .retrieve()
	                    .bodyToMono(String.class)
	                    .block(); 

	            JSONObject json = new JSONObject(response);

	            if (json.has("close")) {
	                return Double.parseDouble(json.getString("close"));
	            } else if (json.has("price")) {
	                return Double.parseDouble(json.getString("price"));
	            } else {
	                throw new RuntimeException("Price field not found in Twelve Data response");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            // Fallback it will generate a random price if API call fails so that some random data is generated here !!!!
	            return 100 + 400 * random.nextDouble();
	        }
	    }


	public double getPrice(String stockSymbol) {
		
		StockPriceCache cache = cacheRepository.findById(stockSymbol).orElse(null);
		if(cache == null || cache.getLastUpdated().isBefore(LocalDateTime.now().minusMinutes(5))) {
			
			double price = fetchLivePrice(stockSymbol);
			StockPriceCache newCache = new StockPriceCache();
			newCache.setStockSymbol(stockSymbol);
			newCache.setPrice(price);
			newCache.setLastUpdated(LocalDateTime.now());
			cacheRepository.save(newCache);
			return price;
		}
		return cache.getPrice();
	}
}


