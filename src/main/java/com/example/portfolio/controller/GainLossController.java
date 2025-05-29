package com.example.portfolio.controller;
import com.example.portfolio.dto.GainLossResponseDTO;
import com.example.portfolio.model.GainLoss;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.service.GainLossService;
import com.example.portfolio.repository.PortfolioRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// Uses RestController and RequestMapping to help provide a mapped endpoint for the api to obtain gain/loss for a user's portfolio
@RestController
@RequestMapping("/api/gain-loss")
public class GainLossController {
	private static final Logger logger = LoggerFactory.getLogger(GainLossController.class);
	@Autowired
	private final GainLossService gainLossService;
	private final PortfolioRepository portfolioRepository;
	public GainLossController(GainLossService gainLossService,PortfolioRepository portfolioRepository) {
		this.gainLossService=gainLossService;
		this.portfolioRepository=portfolioRepository;
	}
	
	// Displays the calculated total gain/loss and percentage for a user's portfolio id while checking if portfolio exists
	@GetMapping("/{portfolioId}")
	public List<GainLossResponseDTO> calculateGainLoss(@PathVariable Long portfolioId) {
		logger.info("Received request to calculate gain/loss for portfolio ID: {}",portfolioId);
		Portfolio portfolio=portfolioRepository.findById(portfolioId)
				.orElseThrow(()->{
					logger.error("Portfolio not found with ID: {}",portfolioId);
					return new RuntimeException("Portfolio not found");
					});
		List<GainLoss> gainLoss=gainLossService.calculateGainLoss(portfolio);
		logger.debug("Calculated gain/loss for portfolio ID {}: {}",portfolioId,gainLoss);
		List<GainLossResponseDTO> response = gainLoss.stream()
				.map(gl->new GainLossResponseDTO(gl.getHolding().getStockSymbol(),gl.getGain(),gl.getPercentage()))
				.collect(Collectors.toList());
		logger.info("Returning gain/loss response for portfolio ID {}: {}",portfolioId,response);
		return response;
	}
}