package com.example.portfolio.controller;

import com.example.portfolio.model.Alert;
import com.example.portfolio.model.NotificationLog;
import com.example.portfolio.service.AlertService;
import com.example.portfolio.service.NotificationLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationLogController {

    private final NotificationLogService notificationLogService;
    private final AlertService alertService;

    public NotificationLogController(NotificationLogService notificationLogService, AlertService alertService) {
        this.notificationLogService = notificationLogService;
        this.alertService = alertService;
    }

    @GetMapping("/notification-log")
    public ResponseEntity<List<NotificationLog>> getAllLogs() {
        return ResponseEntity.ok(notificationLogService.getAllLogs());
    }

    @GetMapping("/alert/{alertId}")
    public ResponseEntity<List<NotificationLog>> getLogsByAlert(@PathVariable Long alertId) {
        Alert alert = alertService.getAlertById(alertId);
        return ResponseEntity.ok(notificationLogService.getLogByAlert(alert));
    }

    @GetMapping("/method/{method}")
    public ResponseEntity<List<NotificationLog>> getLogsByMethod(@PathVariable String method) {
        return ResponseEntity.ok(notificationLogService.getLogByMethod(method));
    }
}
