package com.portfolio.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.portfolio.model.Holding;
import com.portfolio.model.Portfolio;
import com.portfolio.model.User;
import com.portfolio.repository.HoldingRepository;
import com.portfolio.repository.PortfolioRepository;
import com.portfolio.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final UserRepository userRepo;
    private final PortfolioRepository portfolioRepo;
    private final HoldingRepository holdingRepo;

    public AdminController(UserRepository userRepo, PortfolioRepository portfolioRepo, HoldingRepository holdingRepo) {
        this.userRepo = userRepo;
        this.portfolioRepo = portfolioRepo;
        this.holdingRepo = holdingRepo;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
    	logger.info("Fetching all users");
    	List<User> users = userRepo.findAll();
    	logger.debug("Found {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/portfolios")
    public ResponseEntity<List<Portfolio>> getAllPortfolios() {
    	logger.info("Fetching all portfolios");
    	List<Portfolio> portfolios = portfolioRepo.findAll();
    	logger.debug("Found {} portfolios", portfolios.size());
        return ResponseEntity.ok(portfolios);
    }

    @GetMapping("/holdings")
    public ResponseEntity<List<Holding>> getAllHoldings() {
    	logger.info("Fetching all holdings");
    	List<Holding> holdings = holdingRepo.findAll();
    	logger.debug("Found {} holdings", holdings.size());
        return ResponseEntity.ok(holdings);
    }
}

