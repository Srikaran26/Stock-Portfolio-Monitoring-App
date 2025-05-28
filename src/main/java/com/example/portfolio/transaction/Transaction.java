package com.example.portfolio.transaction;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

import com.example.portfolio.model.Portfolio;

import jakarta.persistence.*;

@Entity
@Table(name= "trascations")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String stockSymbol;
	private int quantity;
	private double price;
	private LocalDate date;
	
	
	@Enumerated(EnumType.STRING)
	private TransactionType type;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name= "portfolio_id")
	private Portfolio portfolio;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStockSymbol() {
		return stockSymbol;
	}

	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio protfolio) {
		this.portfolio = protfolio;
	}
	
	
}
