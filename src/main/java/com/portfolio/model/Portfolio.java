package com.portfolio.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


// Each Portfolio belongs to one user which stores basic info like name and description.
@Entity
@Table(name = "portfolios")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Portfolio {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;
	private String name;
	private String description;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	private LocalDateTime createdAt = LocalDateTime.now();
	@OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Holding> holdings;
	// Getters and Setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public List<Holding> getHoldings() {
		return holdings;
	}

	public void setHoldings(List<Holding> holdings) {
		this.holdings = holdings;
	}
}