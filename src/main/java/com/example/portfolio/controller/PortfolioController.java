package com.example.portfolio.controller;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.model.User;
import com.example.portfolio.model.Holding;
import com.example.portfolio.service.PortfolioService;
import com.example.portfolio.repository.HoldingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/portfolios")

public class PortfolioController{
	private final PortfolioService portfolioService;
	private final HoldingRepository holdingRepository;
	@Autowired
	public PortfolioController(PortfolioService portfolioService, HoldingRepository holdingRepository) {
		this.portfolioService = portfolioService;
		this.holdingRepository = holdingRepository;
	}
	// List of all the portfolios for the user.
	@GetMapping
	public List<Portfolio> getPortfolios(@RequestParam Long userId){
		User user = new User();
		user.setId(userId);
		return portfolioService.listPortfolios(user);
	}
	// Creating a new Portfolio.
	@PostMapping
	public Portfolio createPortfolio(@RequestParam Long userId,@RequestParam String name, @RequestParam String description) {
		User user = new User();
		user.setId(userId);
		return portfolioService.createPortfolio(user,  name,  description);
	}
	//Getting the holdings for a random portfolio.
	@GetMapping("/{id}/holdings")
	public List<Holding> getHoldingsByPortfolio(@PathVariable Long portfolioId){
		return holdingRepository.findByPortfolioId(portfolioId);
	}
	//Deleting the portfolio
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePortfolio(@PathVariable Long portfolioId, @RequestParam String username){
		portfolioService.deletePortfolio(portfolioId, username);
		return ResponseEntity.ok("Portfolio deleted successfully.");
	}
}
