package com.portfolio.transaction.dto;

import java.time.LocalDate;

import com.portfolio.transaction.TransactionType;

public class TransactionResponseDTO {
	
	private Long id;
	private String stockSymbol;
	private int quantity;
	private double price;
	private LocalDate date;
	private TransactionType type;
	private Long portfolioId;
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
	public Long getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(Long portfolioId) {
		this.portfolioId = portfolioId;
	}
	
	
}
