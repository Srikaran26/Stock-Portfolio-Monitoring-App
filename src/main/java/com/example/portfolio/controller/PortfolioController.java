package com.example.portfolio.controller;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.model.User;
import com.example.portfolio.model.Holding;
import com.example.portfolio.service.PortfolioService;
import com.example.portfolio.repository.HoldingRepository;
import com.example.portfolio.repository.PortfolioRepository;
import com.example.portfolio.dto.PortfolioRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/portfolios")

public class PortfolioController{
	private static final Logger logger = LoggerFactory.getLogger(PortfolioController.class);

	private final PortfolioService portfolioService;
	private final HoldingRepository holdingRepository;
	private PortfolioRepository portfolioRepository;
	@Autowired
	public PortfolioController(PortfolioService portfolioService, HoldingRepository holdingRepository, PortfolioRepository portfolioRepository) {
		this.portfolioService = portfolioService;
		this.holdingRepository = holdingRepository;
		this.portfolioRepository = portfolioRepository;
	}
	// Provides a list of all the portfolios for the user.
	@GetMapping
	public List<Portfolio> getPortfolios(@RequestParam Long userId){
		logger.info("Fetching portfolios for userId: {}", userId);
		User user = new User();
		user.setId(userId);
		return portfolioService.listPortfolios(user);
	}
	// Creating a new Portfolio.
	@PostMapping
	public Portfolio createPortfolio(@RequestParam Long userId,@RequestParam String name, @RequestParam String description) {
		logger.info("Creating portfolio for userId: {}, name: {}", userId, name);
		User user = new User();
		user.setId(userId);
		return portfolioService.createPortfolio(user,  name,  description);
	}
	// Creating multiple portfolios for a user
	@PostMapping("/bulk")
	public List<Portfolio> createMultiplePortfolios(@RequestBody List<PortfolioRequest> requests) {
		logger.info("Creating {} portfolios in bulk", requests.size());
		return portfolioService.createMultiplePortfolios(requests);
	}
	// Updating the portfolio for the user based on id
	@PutMapping("/{id}")
	public Portfolio updatePortfolio(@PathVariable Long portfolioId, @RequestParam String username, @RequestParam String name, @RequestParam String description) {
		logger.info("Updating portfolio with ID: {}", portfolioId, username);
		return portfolioService.updatePortfolio(portfolioId,username,name,description);
	}
	//Deleting the portfolio based on the id
	@DeleteMapping("/{id}") 
	public String deletePortfolio(@PathVariable Long portfolioId, @RequestParam String username){
		logger.info("Deleting portfolio with ID: {} for user: {}" , portfolioId, username);
		portfolioService.deletePortfolioByIdAndUser(portfolioId, username);
		logger.info("Portfolio with ID: {} deleted successfully", portfolioId);
		return "Portfolio deleted successfully.";
	}
	//Displays the holdings based on the user's portfolio id
	@GetMapping("/{id}/holdings")
	public List<Holding> getHoldingsByPortfolio(@PathVariable("id") Long portfolioId) {
        logger.info("Fetching holdings for portfolio ID: {}", portfolioId);
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> {
                    logger.warn("Portfolio ID {} not found", portfolioId);
                    return new RuntimeException("Portfolio not found");
                });
        return holdingRepository.findByPortfolio(portfolio);
	}	
}
