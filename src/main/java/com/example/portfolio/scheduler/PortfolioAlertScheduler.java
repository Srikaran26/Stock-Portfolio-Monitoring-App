package com.example.portfolio.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.portfolio.model.Portfolio;
import com.example.portfolio.model.PortfolioAlert;
import com.example.portfolio.repository.HoldingRepository;
import com.example.portfolio.repository.PortfolioAlertRepository;
import com.example.portfolio.service.GainLossService;
import com.example.portfolio.service.NotificationLogService;

@Component
public class PortfolioAlertScheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(PortfolioAlertScheduler.class);
	
	private PortfolioAlertRepository portfolioAlertRepository;
	private HoldingRepository holdingRepository;
	private NotificationLogService notificationLogService;
	private GainLossService gainLossService;
	
	public PortfolioAlertScheduler(PortfolioAlertRepository portfolioAlertRepository, HoldingRepository holdingRepository,NotificationLogService notificationLogService, GainLossService gainLossService) {
		this.portfolioAlertRepository = portfolioAlertRepository;
		this.holdingRepository = holdingRepository;
		this.notificationLogService = notificationLogService;
		this.gainLossService = gainLossService;
	}
	
	@Scheduled(fixedRate = 60000)
	public void checkPortfolioAlerts() {
		
		logger.info("Running scheduled check for portfolio alerts.");
		
		List<PortfolioAlert> alerts = portfolioAlertRepository.findByActiveTrue();
		
		for(PortfolioAlert alert : alerts) {
			Portfolio portfolio = alert.getPortfolio();
			
			double totalGainLoss = gainLossService.calculateGainLoss(portfolio);
			
			boolean conditionMet = totalGainLoss>=alert.getThreshold();
			
			if(!alert.isTriggered() && conditionMet) {
				String message = "Portfolio alert triggered for user '" + portfolio.getUser().getUsername() + "': Gain/Loss = " + totalGainLoss + ", Threshold = " + alert.getThreshold();
				notificationLogService.logNotification(null,message, "PORTFOLIO_ALERT");
				logger.info(message);
				alert.setTriggered(true);
				alert.setActive(true);
				
				portfolioAlertRepository.save(alert);
			}
		}
		
	}

}
