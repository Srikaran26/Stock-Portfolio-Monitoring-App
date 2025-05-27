package com.example.portfolio.controller;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.model.User;
import com.example.portfolio.service.GainLossService;
import com.example.portfolio.service.PortfolioService;
import com.example.portfolio.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Exposing a mapped endpoint to obtain gain/loss for a user's portfolio
@RestController
@RequestMapping("/api/gainloss")
public class GainLossController {
	private final GainLossService gainLossService;
	private final PortfolioService portfolioService;
	private final UserService userService;
	public GainLossController(GainLossService gainLossService,PortfolioService portfolioService,UserService userService) {
		this.gainLossService=gainLossService;
		this.portfolioService=portfolioService;
		this.userService=userService;
	}
	
	// Provides total gain/loss for a user's portfolio id
	@GetMapping("/{portfolioId}")
	public ResponseEntity<Double> getGainLoss(@RequestParam String username,@PathVariable Long portfolioId) {
		User user=userService.getByUsername(username).orElseThrow();
		Portfolio portfolio=portfolioService.getPortfolioByIdAndUser(portfolioId,username);
		double gainLoss=gainLossService.calculateGainLoss(portfolio);
		return ResponseEntity.ok(gainLoss);
	}
}