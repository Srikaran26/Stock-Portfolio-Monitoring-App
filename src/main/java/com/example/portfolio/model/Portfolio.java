package com.example.portfolio.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
public class Portfolio {
	private Long id;
	private String name;
	private String description;
	private User user;
	private LocalDateTime createdAt = LocalDateTime.now();
}
