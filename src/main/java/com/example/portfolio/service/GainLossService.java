package com.example.portfolio.service;
import com.example.portfolio.model.Portfolio;
import com.example.portfolio.model.GainLoss;
import java.util.List;
// Interface for calculating total gain/loss for an investment portfolio
public interface GainLossService {
	List<GainLoss> calculateGainLoss(Portfolio portfolio);
}