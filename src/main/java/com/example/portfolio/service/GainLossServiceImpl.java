package com.example.portfolio.service;

import com.example.portfolio.model.GainLoss;
import com.example.portfolio.model.Holding;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.repository.GainLossRepository;
import com.example.portfolio.repository.HoldingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Implementing gainLossService interface for computing gain/loss in a user's portfolio
@Service
public class GainLossServiceImpl implements GainLossService {
		private final HoldingRepository holdingRepository;
		private final StockPriceService stockPriceService;
		private final GainLossRepository gainLossRepository;
		
		//	Providing the current instance values of the user's portfolio 
		@Autowired
		public GainLossServiceImpl(HoldingRepository holdingRepository, StockPriceService stockPriceService, GainLossRepository gainLossRepository) {
			this.holdingRepository=holdingRepository;
			this.stockPriceService=stockPriceService;
			this.gainLossRepository=gainLossRepository;
		}
		
		// Formula for calculating gain/loss in the given investment portfolio
		@Override
		public List<GainLoss> calculateGainLoss(Portfolio portfolio) {
			List<Holding> holdings=holdingRepository.findByPortfolio(portfolio);
			List<GainLoss> results=new ArrayList<>();
			for (Holding h : holdings) {
				double currentPrice =stockPriceService.getPrice(h.getStockSymbol());
				double gain = (currentPrice-h.getBuyPrice())*h.getQuantity();
				double invested = h.getBuyPrice() * h.getQuantity();
				double percentage = invested != 0 ? (gain/invested) * 100 : 0.0;
				
				GainLoss record=new GainLoss(h,gain,percentage);
				gainLossRepository.save(record);
				results.add(record);
		}
		return results;
	}
}
