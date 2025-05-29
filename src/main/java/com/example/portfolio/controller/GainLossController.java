package com.example.portfolio.controller;
import com.example.portfolio.dto.GainLossResponseDTO;
import com.example.portfolio.model.GainLoss;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.service.GainLossService;
import com.example.portfolio.repository.PortfolioRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// Uses RestController and RequestMapping to help provide a mapped endpoint for the api to obtain gain/loss for a user's portfolio
@RestController
@RequestMapping("/api/gain-loss")
public class GainLossController {
	@Autowired
	private final GainLossService gainLossService;
	private final PortfolioRepository portfolioRepository;
	public GainLossController(GainLossService gainLossService,PortfolioRepository portfolioRepository) {
		this.gainLossService=gainLossService;
		this.portfolioRepository=portfolioRepository;
	}
	
	// Displays the calculated total gain/loss and percentage for a user's portfolio id
	@GetMapping("/{portfolioId}")
	public List<GainLossResponseDTO> calculateGainLoss(@PathVariable Long portfolioId) {
		Portfolio portfolio=portfolioRepository.findById(portfolioId).orElseThrow(()->new RuntimeException("Portfolio not found"));
		List<GainLoss> gainLoss=gainLossService.calculateGainLoss(portfolio);
		return gainLoss.stream()
				.map(gl->new GainLossResponseDTO(gl.getHolding().getStockSymbol(),gl.getGain(),gl.getPercentage()))
				.collect(Collectors.toList());
	}
}