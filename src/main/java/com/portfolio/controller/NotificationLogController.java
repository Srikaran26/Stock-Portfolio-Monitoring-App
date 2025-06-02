package com.portfolio.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.portfolio.model.Alert;
import com.portfolio.model.NotificationLog;
import com.portfolio.service.AlertService;
import com.portfolio.service.NotificationLogService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationLogController {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationLogController.class);

    private final NotificationLogService notificationLogService;
    private final AlertService alertService;

    public NotificationLogController(NotificationLogService notificationLogService, AlertService alertService) {
        this.notificationLogService = notificationLogService;
        this.alertService = alertService;
    }

    @GetMapping("/notification-log")
    public ResponseEntity<List<NotificationLog>> getAllLogs() {
    	logger.info("Getting all notification logs");
        return ResponseEntity.ok(notificationLogService.getAllLogs());
    }

    @GetMapping("/alert/{alertId}")
    public ResponseEntity<List<NotificationLog>> getLogsByAlert(@PathVariable Long alertId) {
    	logger.info("Getting notification logs by alerts");
        Alert alert = alertService.getAlertById(alertId);
        return ResponseEntity.ok(notificationLogService.getLogByAlert(alert));
    }

    @GetMapping("/method/{method}")
    public ResponseEntity<List<NotificationLog>> getLogsByMethod(@PathVariable String method) {
    	logger.info("Gettings logs using methods");
        return ResponseEntity.ok(notificationLogService.getLogByMethod(method));
    }
}
