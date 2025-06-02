package com.portfolio.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.model.StockPriceCache;

public interface StockPriceCacheRepository extends JpaRepository<StockPriceCache, String>{
	
}


