package com.example.portfolio.repository;


import com.example.portfolio.model.StockPriceCache;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPriceCacheRepository extends JpaRepository<StockPriceCache, String>{
	
}


