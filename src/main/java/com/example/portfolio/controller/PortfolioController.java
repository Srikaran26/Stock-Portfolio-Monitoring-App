package com.example.portfolio.controller;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.model.User;
import com.example.portfolio.model.Holding;
import com.example.portfolio.service.PortfolioService;
import com.example.portfolio.repository.HoldingRepository;
import com.example.portfolio.repository.PortfolioRepository;
import com.example.portfolio.dto.PortfolioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/portfolios")

public class PortfolioController{

	private final PortfolioService portfolioService;
	private final HoldingRepository holdingRepository;
	private PortfolioRepository portfolioRepository;
	@Autowired
	public PortfolioController(PortfolioService portfolioService, HoldingRepository holdingRepository, PortfolioRepository portfolioRepository) {
		this.portfolioService = portfolioService;
		this.holdingRepository = holdingRepository;
		this.portfolioRepository = portfolioRepository;
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
	// Creating multiple portfolios for a user
	@PostMapping("/bulk")
	public List<Portfolio> createMultiplePortfolios(@RequestBody List<PortfolioRequest> requests) {
		return portfolioService.createMultiplePortfolios(requests);
	}
	// Updating the portfolio for the user
	@PutMapping("/{id}")
	public Portfolio updatePortfolio(@PathVariable Long portfolioId, @RequestParam String username, @RequestParam String name, @RequestParam String description) {
		return portfolioService.updatePortfolio(portfolioId,username,name,description);
	}
	//Deleting the portfolio
	@DeleteMapping("/{id}")
	public String deletePortfolio(@PathVariable Long portfolioId, @RequestParam String username){
		portfolioService.deletePortfolio(portfolioId, username);
		return "Portfolio deleted successfully.";
	}
	//Getting the holdings for a random portfolio
	@GetMapping("/{id}/holdings")
	public List<Holding> getHoldingsByPortfolio(@PathVariable("id") Long portfolioId) {
		Portfolio portfolio=portfolioRepository.findById(portfolioId)
				.orElseThrow(()->new RuntimeException("Portfolio not found"));
		return holdingRepository.findByPortfolio(portfolio);
	}	
}
