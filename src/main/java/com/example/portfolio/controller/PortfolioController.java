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
	@GetMapping
	public List<Portfolio> getPortfolios(@RequestParam Long userId){
		logger.info("Fetching portfolios for userId: {}", userId);
		User user = new User();
		user.setId(userId);
		return portfolioService.listPortfolios(user);
	}

	@PostMapping("/single")
	public Portfolio createPortfolio(@RequestBody PortfolioRequest request) {
		logger.info("Creating portfolio for userId: {}, name: {}", request.getUserId(), request.getName());
		User user = new User();
		user.setId(request.getUserId());
		return portfolioService.createPortfolio(user, request.getName(),  request.getDescription());
	}

	@PostMapping("/bulk")
	public List<Portfolio> createMultiplePortfolios(@RequestBody List<PortfolioRequest> requests) {
		logger.info("Creating {} portfolios in bulk", requests.size());
		return portfolioService.createMultiplePortfolios(requests);
	}

	@PutMapping("/{id}")
	public Portfolio updatePortfolio(@PathVariable Long portfolioId, @RequestParam String username, @RequestParam String name, @RequestParam String description) {
		logger.info("Updating portfolio with ID: {}", portfolioId, username);
		return portfolioService.updatePortfolio(portfolioId,username,name,description);
	}
	@DeleteMapping("/{id}") 
	public String deletePortfolio(@PathVariable Long portfolioId, @RequestParam String username){
		logger.info("Deleting portfolio with ID: {} for user: {}" , portfolioId, username);
		portfolioService.deletePortfolioByIdAndUser(portfolioId, username);
		logger.info("Portfolio with ID: {} deleted successfully", portfolioId);
		return "Portfolio deleted successfully.";
	}
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
