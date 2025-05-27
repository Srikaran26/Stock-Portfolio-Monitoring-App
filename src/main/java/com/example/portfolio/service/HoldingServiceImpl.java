package com.example.portfolio.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.example.portfolio.model.Holding;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.repository.HoldingRepository;

@Service
public class HoldingServiceImpl implements HoldingService{
	
	private final HoldingRepository holdingRepository;
	
	public HoldingServiceImpl ( HoldingRepository holdingRepository ) {
		this.holdingRepository = holdingRepository;
	}
	
	public Holding addHolding (Portfolio portfolio, String stockSymbol, int quantity, double buyPrice) {
		Holding h = new Holding();
		h.setPortfolio(portfolio);
		h.setStockSymbol(stockSymbol);
		h.setQuantity(quantity);
		h.setBuyPrice(buyPrice);
		return holdingRepository.save(h);
	}
	
	public List<Holding> getHoldings(Portfolio portfolio){
		return holdingRepository.findByPortfolio(portfolio);
	}
	
	public Holding updateHolding(Long id, int quantity, double buyPrice) {
		Holding h = holdingRepository.findById(id).orElseThrow();
		h.setQuantity(quantity);
		h.setBuyPrice(buyPrice);
		return holdingRepository.save(h);
	}
	
	public void deleteHolding(Long id) {
		holdingRepository.deleteById(id);
	}
}
