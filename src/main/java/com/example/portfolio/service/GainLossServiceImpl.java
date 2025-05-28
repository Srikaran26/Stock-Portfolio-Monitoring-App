package com.example.portfolio.service;

import com.example.portfolio.model.Holding;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.repository.HoldingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementing gainLossService interface for computing gain/loss in a user's portfolio
@Service
public class GainLossServiceImpl implements GainLossService {
		private final HoldingRepository holdingRepository;
		private final StockPriceService stockPriceService;
		
		//	Providing the current instance values of the user's portfolio 
		@Autowired
		public GainLossServiceImpl(HoldingRepository holdingRepository, StockPriceService stockPriceService) {
			this.holdingRepository=holdingRepository;
			this.stockPriceService=stockPriceService;
		}
		
		// Formula for calculating gain/loss in the given investment portfolio
		@Override
		public double calculateGainLoss(Portfolio portfolio) {
			List<Holding> holdings=holdingRepository.findByPortfolio(portfolio);
			double totalGainLoss=0.0;
			for (Holding h : holdings) {
				double currentPrice =stockPriceService.getPrice(h.getStockSymbol());
				totalGainLoss += (currentPrice-h.getBuyPrice())*h.getQuantity();
		}
		return totalGainLoss;
	}
}
