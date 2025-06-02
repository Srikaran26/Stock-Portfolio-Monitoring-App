package com.portfolio.service;

import java.util.List;

import com.portfolio.model.Holding;
import com.portfolio.model.Portfolio;

public interface HoldingService  {
	
	Holding addHolding (Portfolio portfolio, String stockSymbol, int quantity, double buyPrice);
	
	List<Holding> getHoldings(Portfolio portfolio);
	
	Holding updateHolding(Long id, int quantity, double buyPrice);
	
	void deleteHolding(Long id);
	
	Holding getHoldingById(Long id, String username);
}
