package com.example.portfolio.controller;

import com.example.portfolio.model.Alert;
import com.example.portfolio.model.User;
import com.example.portfolio.service.AlertService;
import com.example.portfolio.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;
    private final UserService userService;

    public AlertController(AlertService alertService, UserService userService) {
        this.alertService = alertService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Alert> createAlert(@RequestBody Alert alert) {
        User user = userService.getByUsername(alert.getUser().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Alert created = alertService.createdAlert(user, alert.getStockSymbol(), alert.getTargetPrice(), alert.getAlertType());
        return ResponseEntity.ok(created);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Alert>> listAlertsByUser(@PathVariable String username) {
        User user = userService.getByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(alertService.listUserAlerts(user));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Alert>> getAllActiveAlerts() {
        return ResponseEntity.ok(alertService.getAllActiveAlerts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alert> updateAlert(@PathVariable Long id, @RequestBody Alert updatedAlert) {
        Alert result = alertService.updatedAlert(
                id,
                updatedAlert.getTargetPrice(),
                updatedAlert.getAlertType(),
                updatedAlert.isActive()
        );
        return ResponseEntity.ok(result);
    }
}
