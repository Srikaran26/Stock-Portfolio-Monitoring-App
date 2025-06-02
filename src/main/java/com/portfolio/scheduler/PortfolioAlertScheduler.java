package com.portfolio.scheduler;

import java.util.List;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.portfolio.model.GainLoss;
import com.portfolio.model.Portfolio;
import com.portfolio.model.PortfolioAlert;
import com.portfolio.repository.HoldingRepository;
import com.portfolio.repository.PortfolioAlertRepository;
import com.portfolio.service.GainLossService;
import com.portfolio.service.NotificationLogService;

@Component
public class PortfolioAlertScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioAlertScheduler.class);

    private final PortfolioAlertRepository portfolioAlertRepository;
    private final HoldingRepository holdingRepository;
    private final NotificationLogService notificationLogService;
    private final GainLossService gainLossService;

    public PortfolioAlertScheduler(PortfolioAlertRepository portfolioAlertRepository,
                                    HoldingRepository holdingRepository,
                                    NotificationLogService notificationLogService,
                                    GainLossService gainLossService) {
        this.portfolioAlertRepository = portfolioAlertRepository;
        this.holdingRepository = holdingRepository;
        this.notificationLogService = notificationLogService;
        this.gainLossService = gainLossService;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkPortfolioAlerts() {
        logger.info("Running scheduled check for portfolio alerts.");

        List<PortfolioAlert> alerts = portfolioAlertRepository.findByActiveTrue();

        for (PortfolioAlert alert : alerts) {
            Portfolio portfolio = alert.getPortfolio();

            List<GainLoss> gainLossList = gainLossService.calculateGainLoss(portfolio);

            double totalGainLoss = gainLossList.stream()
                    .mapToDouble(GainLoss::getGain)
                    .sum();

            logger.info("Checking portfolio ID: {} - Gain/Loss: {}, Threshold: {}",
                    portfolio.getId(), totalGainLoss, alert.getThreshold());

            boolean conditionMet = totalGainLoss >= alert.getThreshold();

            if (!alert.isTriggered() && conditionMet) {
                String message = "Portfolio alert TRIGGERED for user '" + portfolio.getUser().getUsername()
                        + "': Gain/Loss = " + totalGainLoss + ", Threshold = " + alert.getThreshold();

                notificationLogService.logNotification(null, message, "PORTFOLIO_ALERT");
                logger.info(message);

                alert.setTriggered(true);
                alert.setActive(false); 
                portfolioAlertRepository.save(alert); 
            }
        }
    }
}
