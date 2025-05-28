package com.example.portfolio.service;

import java.util.List;

import com.example.portfolio.model.Alert;
import com.example.portfolio.model.NotificationLog;

public interface NotificationLogService {
	NotificationLog logNotification(Alert alert, String message, String method);
	List<NotificationLog> getLogByAlert(Alert alert);
	List<NotificationLog> getLogByMethod(String method);
	List<NotificationLog> getAllLogs();
}