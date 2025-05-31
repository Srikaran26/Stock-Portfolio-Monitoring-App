package com.example.portfolio.controller;

import com.example.portfolio.dto.HoldingRequestDTO;
import com.example.portfolio.dto.UpdateHoldingRequest;
import com.example.portfolio.model.Holding;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.service.HoldingService;
import com.example.portfolio.service.PortfolioService;
import com.example.portfolio.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holdings")
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

    // ✅ Create Holding (JSON request)
    @PostMapping
    public ResponseEntity<Holding> addHolding(@RequestBody HoldingRequestDTO request) {
        Portfolio portfolio = portfolioService.getPortfolioByIdAndUser(request.getPortfolioId(), request.getUsername());
        Holding holding = holdingService.addHolding(portfolio, request.getStockSymbol(), request.getQuantity(), request.getBuyPrice());
        return ResponseEntity.ok(holding);
    }

    // ✅ Get All Holdings by Portfolio ID and Username
    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<List<Holding>> getHoldings(@RequestParam String username,
                                                     @PathVariable Long portfolioId) {
        Portfolio portfolio = portfolioService.getPortfolioByIdAndUser(portfolioId, username);
        return ResponseEntity.ok(holdingService.getHoldings(portfolio));
    }

    // ✅ Update Holding by ID
    @PutMapping("/{id}")
    public ResponseEntity<Holding> updateHolding(@PathVariable Long id,
                                                 @RequestBody UpdateHoldingRequest updateRequest) {
        Holding updated = holdingService.updateHolding(id, updateRequest.getQuantity(), updateRequest.getBuyPrice());
        return ResponseEntity.ok(updated);
    }

    // ✅ Delete Holding by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHolding(@PathVariable Long id) {
        holdingService.deleteHolding(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Get Holding by ID
    @GetMapping("/{id}")
    public ResponseEntity<Holding> getHoldingById(@RequestParam String username,
                                                  @PathVariable Long id) {
        Holding h = holdingService.getHoldingById(id, username);
        return ResponseEntity.ok(h);
    }
}
