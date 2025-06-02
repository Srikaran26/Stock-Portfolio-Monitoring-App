package com.portfolio.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.portfolio.model.Alert;
import com.portfolio.model.User;
import com.portfolio.service.AlertService;
import com.portfolio.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
	
	private static final Logger logger = LoggerFactory.getLogger(AlertController.class);
	
    private final AlertService alertService;
    private final UserService userService;

    public AlertController(AlertService alertService, UserService userService) {
        this.alertService = alertService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Alert> createAlert(@RequestBody Alert alert) {
    	logger.info("T createb alert for a particular user");
        User user = userService.getByUsername(alert.getUser().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Alert created = alertService.createdAlert(user, alert.getStockSymbol(), alert.getTargetPrice(), alert.getAlertType());
        return ResponseEntity.ok(created);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Alert>> listAlertsByUser(@PathVariable String username) {
    	logger.info("Getting user by username to check alerts");
        User user = userService.getByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(alertService.listUserAlerts(user));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Alert>> getAllActiveAlerts() {
    	logger.info("Getting all alerts from active users");
        return ResponseEntity.ok(alertService.getAllActiveAlerts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alert> updateAlert(@PathVariable Long id, @RequestBody Alert updatedAlert) {
    	logger.info("To update alert for a particular user");
        Alert result = alertService.updatedAlert(
                id,
                updatedAlert.getTargetPrice(),
                updatedAlert.getAlertType(),
                updatedAlert.isActive()
        );
        return ResponseEntity.ok(result);
    }
}
