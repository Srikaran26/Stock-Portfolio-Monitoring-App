package com.example.portfolio.controller;

import org.springframework.web.bind.annotation.*;

import com.example.portfolio.model.*;
import com.example.portfolio.service.*;
import org.springframework.http.ResponseEntity;
import java.util.*;

@RestController
@RequestMapping("api/holdings")
public class HoldingController {
	private final HoldingService holdingService;
	private final PortfolioService portfolioService;
	private final UserService userService;
	public HoldingController(HoldingService holdingService, PortfolioService portfolioService,
			UserService userService) {
		
		this.holdingService = holdingService;
		this.portfolioService = portfolioService;
		this.userService = userService;
	}
	
	@PostMapping
	public ResponseEntity<Holding> addHolding(@RequestParam String username,
			                                  @RequestParam Long portfolioId,
			                                  @RequestParam String stockSymbol,
			                                  @RequestParam int quantity,
			                                  @RequestParam double buyPrice){
		Portfolio portfolio = portfolioService.getPortfolioByIdAndUser(portfolioId, username);
		Holding holding = holdingService.addHolding(portfolio, stockSymbol, quantity, buyPrice);
		return ResponseEntity.ok(holding);
	}
	
	@GetMapping("/{portfolioId}")
	public ResponseEntity<List<Holding>>getHoldings(@RequestParam String username,
			                                        @PathVariable Long portfolioId){
		Portfolio portfolio = portfolioService.getPortfolioByIdAndUser(portfolioId, username);
		return ResponseEntity.ok(holdingService.getHoldings(portfolio));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Holding>updateHolding(@PathVariable Long id,
			                                     @RequestParam int quantity,
			                                     @RequestParam double buyPrice){
		Holding updated = holdingService.updateHolding(id,quantity,buyPrice);
		return ResponseEntity.ok(updated);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Holding>deleteHolding(@PathVariable Long id){
		holdingService.deleteHolding(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/holdings/{id}")
	public ResponseEntity<Holding> getHoldingById(@RequestParam String username,
			                                      @PathVariable Long id){
		Holding h = holdingService.getHoldingById(id, username);
		return ResponseEntity.ok(h);
	}
	
}
