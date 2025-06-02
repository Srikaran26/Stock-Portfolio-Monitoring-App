package com.portfolio.controller;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.portfolio.model.User;
import com.portfolio.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
     // registration endpoint
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
    	logger.info("Attempting to register user: {}", user.getUsername());
        User savedUser = userService.registerUser(user);
        logger.info("User registered successfully: {}",savedUser.getUsername());
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
     //login endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
    	logger.info("Login attempt for user: {}", user.getUsername());
        boolean success = userService.login(user.getUsername(), user.getPassword());
        if (success) {
        	logger.info("Login successful for user: {}", user.getUsername());
            return ResponseEntity.ok("Login successful");
        } else {
        	logger.warn("Login failed for user: {}",user.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
