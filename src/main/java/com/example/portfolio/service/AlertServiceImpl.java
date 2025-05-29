package com.example.portfolio.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.portfolio.exception.AlertNotFoundException;
import com.example.portfolio.model.Alert;
import com.example.portfolio.model.User;
import com.example.portfolio.repository.AlertRepository;

@Service
public class AlertServiceImpl implements AlertService {
	private AlertRepository alertRepository;
	
	public AlertServiceImpl(AlertRepository alertRepository) {
		this.alertRepository = alertRepository;
	}
	@Override
	public Alert createdAlert(User user, String stockSymbol, double targetPrice, String alertType) {
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
		return alertRepository.findByUser(user);
	}
	public List<Alert> getAllActiveAlerts() {
	    return alertRepository.findByActiveTrue();
	}
	@Override
	public Alert getAlertById(Long id) {
	    return alertRepository.findById(id).orElseThrow(() -> new AlertNotFoundException("Alert not found"));
	}
	
	public Alert updatedAlert(Long id, Double targetPrice, String alertType, Boolean active) {
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
