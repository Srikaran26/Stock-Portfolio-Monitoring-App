package com.example.portfolio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.portfolio.model.Holding;
import com.example.portfolio.service.PortfolioService;
import com.example.portfolio.service.UserService;
import com.example.portfolio.service.HoldingService;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("api/reports")
public class ReportingController {
	private final PortfolioService portfolioService;
	private final UserService userService;
	private final HoldingService holdingSevice;
	
	public ReportingController(PortfolioService portfolioService, UserService userService) {
		this.portfolioService=portfolioService;
		this.userService=userService;
		this.holdigService=holdingService;
		
	}
	
	@GetMapping("/Portfolio-summary")
	public ResponseEntity<String> getPortfolioSummary(@RequestParam String username, @RequestParam Long portfolioId){
		User user = userService.getByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
		Portfolio portfolio= portfolioService.getPortfolioByIdAndUser(portfolioId, username);
		List<Holding> holdings = holdingService.getHoldings(portfolio);
		int numberOfHoldings = holdings.size();
		
		double totalValue= holdings.stream()
				.mapToDouble(holding ->{
					double currentPrice = portfolioService.getCurrentStockPrice(holding.getStockSymbol());
	                return currentPrice * holding.getQuantity();
				})
				.sum();
		
		String summary = String.format("Portfolio '%s' has %d holdings and total value %.2f.",
                portfolio.getName(), numberOfHoldings, totalValue);
		
		return ResponseEntity.ok(summary);
	}
	
}
