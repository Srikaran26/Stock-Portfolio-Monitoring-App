package com.example.portfolio.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.portfolio.exception.AlertNotFoundException;
import com.example.portfolio.model.Alert;
import com.example.portfolio.model.User;
import com.example.portfolio.repository.AlertRepository;

@Service
public class AlertServiceImpl implements AlertService {
	private static final Logger logger = LoggerFactory.getLogger(AlertServiceImpl.class);
	private AlertRepository alertRepository;
	
	public AlertServiceImpl(AlertRepository alertRepository) {
		this.alertRepository = alertRepository;
	}
	@Override
	public Alert createdAlert(User user, String stockSymbol, double targetPrice, String alertType) {
		logger.info("Setting Alert");
		Alert alert = new Alert();
		alert.setUser(user);
		alert.setStockSymbol(stockSymbol);
		alert.setTargetPrice(targetPrice);
		alert.setAlertType(alertType);
		alert.setActive(true);
		return alertRepository.save(alert);
	}
	@Override
	public List<Alert> listUserAlerts(User user){
		logger.info("To find alerts by user");
		return alertRepository.findByUser(user);
	}
	public List<Alert> getAllActiveAlerts() {
		logger.info("To find alerts by active true");
	    return alertRepository.findByActiveTrue();
	}
	@Override
	public Alert getAlertById(Long id) {
		logger.info("To find alert by id");
	    return alertRepository.findById(id).orElseThrow(() -> new AlertNotFoundException("Alert not found"));
	}
	
	public Alert updatedAlert(Long id, Double targetPrice, String alertType, Boolean active) {
		logger.info("Alert not found for a particular id");
		Alert alert = alertRepository.findById(id).orElseThrow(() -> new AlertNotFoundException("Alert with ID " + id + " not found"));
		
		if(targetPrice != null) {
			alert.setTargetPrice(targetPrice);
		}
		
		if(alertType != null) {
			alert.setAlertType(alertType);
		}
		
		if(active != null) {
			alert.setActive(active);
		}
		return alertRepository.save(alert);
	}


}
