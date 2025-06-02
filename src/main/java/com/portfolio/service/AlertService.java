package com.portfolio.service;

import java.util.List;

import com.portfolio.model.Alert;
import com.portfolio.model.User;

public interface AlertService {
	Alert createdAlert(User user, String stockSymbol, double targetPrice, String alertType);
	List<Alert> listUserAlerts(User user);
	Alert getAlertById(Long id);
	List<Alert> getAllActiveAlerts();
	Alert updatedAlert(Long id, Double targetPrice, String alertType, Boolean active);

}
