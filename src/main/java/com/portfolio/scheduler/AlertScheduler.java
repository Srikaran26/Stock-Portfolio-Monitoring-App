package com.portfolio.scheduler;

import java.util.List;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.portfolio.model.Alert;
import com.portfolio.service.AlertServiceImpl;
import com.portfolio.service.NotificationLogService;
import com.portfolio.service.StockPriceService;

@Component
public class AlertScheduler {

    private static final Logger logger = LoggerFactory.getLogger(AlertScheduler.class);
    private final AlertServiceImpl alertService;
    private final StockPriceService stockPriceService;
    private final NotificationLogService notificationLogService;

    public AlertScheduler(AlertServiceImpl alertService, StockPriceService stockPriceService, NotificationLogService notificationLogService) {
        this.alertService = alertService;
        this.stockPriceService = stockPriceService;
        this.notificationLogService = notificationLogService;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkStockAlerts() {
        logger.info("Running scheduled alert check.");
        List<Alert> activeAlerts = alertService.getAllActiveAlerts();

        for (Alert alert : activeAlerts) {
            double currentPrice = stockPriceService.getPrice(alert.getStockSymbol());

            logger.info("Evaluating alert: {} with target price: {}, current price: {}, alert type: {}",
                    alert.getStockSymbol(), alert.getTargetPrice(), currentPrice, alert.getAlertType());

            boolean triggered = false;

            if ("ABOVE".equalsIgnoreCase(alert.getAlertType()) && currentPrice > alert.getTargetPrice()) {
                triggered = true;
            } else if ("BELOW".equalsIgnoreCase(alert.getAlertType()) && currentPrice < alert.getTargetPrice()) {
                triggered = true;
            }

            if (triggered) {
                String message = "ALERT TRIGGERED for " + alert.getStockSymbol() + " at price: " + currentPrice;
                notificationLogService.logNotification(alert, message, "Stock_Alert");

                logger.info(message);

                
                alert.setActive(false);
                alertService.updatedAlert(alert.getId(), alert.getTargetPrice(), alert.getAlertType(), false); // updates active status
            }
        }
    }
}
