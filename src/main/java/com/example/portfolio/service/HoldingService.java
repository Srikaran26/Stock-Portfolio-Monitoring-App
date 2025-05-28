package com.example.portfolio.service;

import java.util.List;

import com.example.portfolio.model.Holding;
import com.example.portfolio.model.Portfolio;

public interface HoldingService  {
	
	Holding addHolding (Portfolio portfolio, String stockSymbol, int quantity, double buyPrice);
	
	List<Holding> getHoldings(Portfolio portfolio);
	
	Holding updateHolding(Long id, int quantity, double buyPrice);
	
	void deleteHolding(Long id);
}
