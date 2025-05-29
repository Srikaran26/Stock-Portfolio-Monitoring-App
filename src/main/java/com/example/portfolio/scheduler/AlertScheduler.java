package com.example.portfolio.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.portfolio.model.Alert;
import com.example.portfolio.service.AlertServiceImpl;
import com.example.portfolio.service.NotificationLogService;
import com.example.portfolio.service.StockPriceService;

@Component
public class AlertScheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(AlertScheduler.class);
	private AlertServiceImpl alertService;
	private StockPriceService stockPriceService;
	private NotificationLogService notificationLogService;
	
	public AlertScheduler(AlertServiceImpl alertService, StockPriceService stockPriceService, NotificationLogService notificationLogService) {
		this.alertService = alertService;
		this.stockPriceService = stockPriceService;
		this.notificationLogService = notificationLogService;
	}
	
	@Scheduled(fixedRate = 60000)
	public void checkStockAlerts() {
		
		logger.info("Running scheduled alert check.");
		
		List<Alert> activeAlerts = alertService.getAllActiveAlerts();
		
		for(Alert alert : activeAlerts) {
			double currentPrice = stockPriceService.getPrice(alert.getStockSymbol());
			
			boolean triggered = false;
			
			if("ABOVE".equalsIgnoreCase(alert.getAlertType())&&currentPrice>alert.getTargetPrice()) {
				triggered = true;
			}
			else if("BELOW".equalsIgnoreCase(alert.getAlertType())&&currentPrice<alert.getTargetPrice()){
				triggered = true;
			}
			
			if(triggered) {
				String message = "Alert for "+alert.getStockSymbol()+" trigerred at price :  "+currentPrice;
				
				notificationLogService.logNotification(alert, message, "Stock_Alert");
				
				logger.info(message);
			}
		}
		
	}
}