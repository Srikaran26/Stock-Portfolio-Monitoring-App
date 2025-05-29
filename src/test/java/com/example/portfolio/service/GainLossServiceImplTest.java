package com.example.portfolio.service;
import com.example.portfolio.model.GainLoss;
import com.example.portfolio.model.Holding;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.repository.GainLossRepository;
import com.example.portfolio.repository.HoldingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class GainLossServiceImplTest {
	private HoldingRepository holdingRepository;
	private StockPriceService stockPriceService;
	private GainLossRepository gainLossRepository;
	private GainLossServiceImpl gainLossService;
	
	@BeforeEach
	public void setUp() {
		holdingRepository=mock(HoldingRepository.class);
		stockPriceService=mock(StockPriceService.class);
		gainLossRepository=mock(GainLossRepository.class);
		
		gainLossService=new GainLossServiceImpl(holdingRepository,stockPriceService,gainLossRepository);
	}
	@Test
	public void testCalculateGainLoss() {
		Portfolio portfolio=new Portfolio();
		Holding h1=new Holding();
		h1.setStockSymbol("AAPL");
		h1.setBuyPrice(100.0);
		h1.setQuantity(10);
		
		Holding h2=new Holding();
		h2.setStockSymbol("GOOGL");
		h2.setBuyPrice(200.0);
		h2.setQuantity(5);
		
		List<Holding> holdings = Arrays.asList(h1,h2);
		when(holdingRepository.findByPortfolio(portfolio)).thenReturn(holdings);
		when(stockPriceService.getPrice("AAPL")).thenReturn(150.0);
		when(stockPriceService.getPrice("GOOGL")).thenReturn(180.0);
		
		List<GainLoss> results=gainLossService.calculateGainLoss(portfolio);
		
		GainLoss gl1=results.get(0);
		assertEquals("AAPL",gl1.getHolding().getStockSymbol());
		assertEquals(500.0,gl1.getGain(),0.001);
		assertEquals(50.0,gl1.getPercentage(),0.001);
		
		GainLoss gl2=results.get(1);
		assertEquals("GOOGL",gl2.getHolding().getStockSymbol());
		assertEquals(-100.0,gl2.getGain(),0.001);
		assertEquals(-10.0,gl2.getPercentage(),0.001);
	}
}