package com.example.portfolio.model;

import jakarta.persistence.*;

//Creating a Entity with table name as "holdings" to store the stocks which are holding by the user.

@Entity
@Table(name = "holdings")
public class Holding {
	
	// We set Id as primary key for this table .
	@Id
	
	// The database will generate a unique primary key value for each new row added to the table, using an identity column . 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	


	// ManyToOne is used here because there are many holdings in a single portfolio .

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "portfolio_id")
	private Portfolio portfolio;
	
	private String stockSymbol;
	private int quantity;
	private double buyPrice;
	private double currentPrice;
	
	// We are creating getters and setters to access the private variable mentioned above .
	
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
	public double getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	
	

}
