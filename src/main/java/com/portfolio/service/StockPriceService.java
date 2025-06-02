package com.portfolio.service;

import java.util.List;

import com.portfolio.model.StockPriceCache;

public interface StockPriceService {
    double getPrice(String symbol);
    double refreshPrice(String symbol);
    List<StockPriceCache> getAllCachedPrices();
    void clearCache();
    boolean isApiAvailable();
    int getCacheSize();
}
