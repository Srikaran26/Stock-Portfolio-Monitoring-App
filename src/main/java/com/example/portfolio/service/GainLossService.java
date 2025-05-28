package com.example.portfolio.service;
import com.example.portfolio.model.Portfolio;

// Interface for calculating total gain/loss for an investment portfolio
public interface GainLossService {
	double calculateGainLoss(Portfolio portfolio);
}