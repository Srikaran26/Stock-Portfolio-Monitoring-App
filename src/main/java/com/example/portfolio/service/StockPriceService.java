package com.example.portfolio.service;

import com.example.portfolio.model.StockPriceCache;

import java.util.List;

public interface StockPriceService {

    double getPrice(String symbol);

    double refreshPrice(String symbol);

    List<StockPriceCache> getAllCachedPrices();

    void clearCache();

    boolean isApiAvailable();

    int getCacheSize();
}
