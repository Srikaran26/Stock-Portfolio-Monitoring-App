package com.example.portfolio.model;

import jakarta.persistence.*;

//Creating a Entity with table name as "holdings" to store the stocks which are holding by the user.
@Entity
@Table(name = "holdings")
public class Holding {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = fetchType.LAZY)
	@JoinColumn(name = "portfolio_id")
	private Portfolio portfolio;
	
	private String stockSymbol;
	private int quantity;
	private double buyPrice;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Portfolio getPortfolio() {
		return portfolio;
	}
	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
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
	public double getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}
	
	

}
