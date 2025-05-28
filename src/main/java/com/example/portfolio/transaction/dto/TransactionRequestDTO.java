package com.example.portfolio.transaction.dto;

import com.example.portfolio.transaction.TransactionType;
import java.time.LocalDate;
public class TransactionRequestDTO {
	private String stockSymbol;
	private int quantity;
	private double price;
	private LocalDate date;
	private TransactionType type;
	private Long PortfolioId;
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
		return PortfolioId;
	}
	public void setPortfolioId(Long portfolioId) {
		PortfolioId = portfolioId;
	}
	
	
}
