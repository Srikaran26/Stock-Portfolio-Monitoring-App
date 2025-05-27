package com.example.portfolio.model;

import java.time.LocalDateTime;
import com.example.portfolio.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "alerts")
@Getter
@Setter
public class Alert {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long id;
	private String stockSymbol;
	private double targetPrice;
	private String alertType;
	private boolean active = true;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	
	private User user;
	
	private LocalDateTime createdAt = LocalDateTime.now();

}
