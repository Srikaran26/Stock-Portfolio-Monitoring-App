package com.example.portfolio.controller;

import com.example.portfolio.service.StockPriceServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-prices")
public class StockPriceController { 
	
	private final StockPriceServiceImpl stockPriceService;
	
	public StockPriceController(StockPriceServiceImpl stockPriceService) {
		this.stockPriceService = stockPriceService;
	}
	
	@GetMapping("/{symbol}/")
    public ResponseEntity<Double> getPrice(@PathVariable String symbol) {
        double price = stockPriceService.getPrice(symbol);
        return ResponseEntity.ok(price);
    }
	
}