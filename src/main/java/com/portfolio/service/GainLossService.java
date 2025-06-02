package com.portfolio.service;
import java.util.List;

import com.portfolio.model.GainLoss;
import com.portfolio.model.Portfolio;
// Interface for calculating total gain/loss for an investment portfolio
public interface GainLossService {
	List<GainLoss> calculateGainLoss(Portfolio portfolio);
}