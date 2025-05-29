package com.example.portfolio.service;

import com.example.portfolio.model.GainLoss;
import com.example.portfolio.model.Holding;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.repository.GainLossRepository;
import com.example.portfolio.repository.HoldingRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Implementing holdingRepository, stockPriceService and gainLossRepository in gainLossService interface for computing gain/loss in a user's portfolio
@Service
public class GainLossServiceImpl implements GainLossService {
	private static final Logger logger = LoggerFactory.getLogger(GainLossServiceImpl.class);
		private final HoldingRepository holdingRepository;
		private final StockPriceService stockPriceService;
		private final GainLossRepository gainLossRepository;
		
		//	Resolves and injects the dependencies shown below for this code 
		@Autowired
		public GainLossServiceImpl(HoldingRepository holdingRepository, StockPriceService stockPriceService, GainLossRepository gainLossRepository) {
			this.holdingRepository=holdingRepository;
			this.stockPriceService=stockPriceService;
			this.gainLossRepository=gainLossRepository;
		}
		
		// Implementation of formula for calculating gain/loss, investment and percentage of gain/loss in the given investment portfolio along with displaying the data from the portfolio
		@Override
		public List<GainLoss> calculateGainLoss(Portfolio portfolio) {
			logger.info("Calculating gain/loss for portfolio ID: {}",portfolio.getId());
			List<Holding> holdings=holdingRepository.findByPortfolio(portfolio);
			logger.debug("Found {} holdings in portfolio ID {}",holdings.size(),portfolio.getId());
			List<GainLoss> results=new ArrayList<>();
			for (Holding h : holdings) {
				double currentPrice =stockPriceService.getPrice(h.getStockSymbol());
				double gain = (currentPrice-h.getBuyPrice())*h.getQuantity();
				double invested = h.getBuyPrice() * h.getQuantity();
				double percentage = invested != 0 ? (gain/invested) * 100 : 0.0;
				
				logger.debug("Holding: {} | BuyPrice: {} | CurrentPrice: {} | Quantity: {} | Gain: {} | Percentage: {}",
						h.getStockSymbol(),h.getBuyPrice(),h.getQuantity(),gain,percentage);
				
				GainLoss record=new GainLoss(h,gain,percentage);
				gainLossRepository.save(record);
				logger.debug("Saved gain/loss record for stock {}",h.getStockSymbol());
				results.add(record);
		}
			logger.info("Gain/loss calculation completed for portfolio ID: {}",portfolio.getId());
			return results;
	}
}
