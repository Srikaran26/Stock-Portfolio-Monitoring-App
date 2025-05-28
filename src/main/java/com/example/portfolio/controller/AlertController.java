package com.example.portfolio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.portfolio.model.Alert;
import com.example.portfolio.model.User;
import com.example.portfolio.service.AlertService;
import com.example.portfolio.service.AlertServiceImpl;
import com.example.portfolio.service.UserService;

@RestController
@RequestMapping("/api/alerts")

public class AlertController {
	@Autowired
	private AlertService alertService;
	private UserService userService;
	
	public AlertController(AlertService alertService, UserService userService) {
		this.alertService = alertService;
		this.userService = userService;
	}
	
	@PostMapping("/")
	public Alert createAlert(@RequestParam String username,
			@RequestParam String stockSymbol,
			@RequestParam double targetPrice,
			@RequestParam String alertType) {
		User user = userService.getByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
		return alertService.createdAlert(user, stockSymbol, targetPrice, alertType);
	}
	
	@GetMapping("/alert")
	public List<Alert> listAlerts(@RequestParam String username){
		User user = userService.getByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
		return alertService.listUserAlerts(user);
	}
	
	@PutMapping("/{id}")
	public Alert updateAlert(@PathVariable Long id,
			@RequestParam(required = false) Double targetPrice,
			@RequestParam(required = false) String alertType,
			@RequestParam(required = false) Boolean active) {
		return alertService.updatedAlert(id,targetPrice,alertType,active);
	}

}
