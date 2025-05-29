package com.example.portfolio.dto;

public class UpdateHoldingRequest {
	
	private int quantity;
	
	private double buyPrice;
	public UpdateHoldingRequest() {
		
		
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
