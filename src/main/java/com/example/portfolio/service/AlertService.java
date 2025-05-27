package com.example.portfolio.service;

import java.util.List;

import com.example.portfolio.model.Alert;
import com.example.portfolio.model.User;

public interface AlertService {
	Alert createdAlert(User user, String stockSymbol, double targetPrice, String alertType);
	List<Alert> listUserAlerts(User user);
	Alert updatedAlert(Long id, Double targetPrice, String alertType, Boolean active);

}
