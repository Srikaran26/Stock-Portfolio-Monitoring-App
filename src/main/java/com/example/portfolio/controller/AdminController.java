package com.example.portfolio.controller;

import com.example.portfolio.model.User;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.model.Holding;
import com.example.portfolio.repository.UserRepository;
import com.example.portfolio.repository.PortfolioRepository;
import com.example.portfolio.repository.HoldingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

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
        return ResponseEntity.ok(userRepo.findAll());
    }

    @GetMapping("/portfolios")
    public ResponseEntity<List<Portfolio>> getAllPortfolios() {
        return ResponseEntity.ok(portfolioRepo.findAll());
    }

    @GetMapping("/holdings")
    public ResponseEntity<List<Holding>> getAllHoldings() {
        return ResponseEntity.ok(holdingRepo.findAll());
    }
}

