package com.example.portfolio.dto;

public class GainLossResponseDTO {
	private String stockSymbol;
	private double gain;
	private double percentage;
	public GainLossResponseDTO(String stockSymbol,double gain,double percentage) {
		this.setStockSymbol(stockSymbol);
		this.setGain(gain);
		this.setPercentage(percentage);
	}
	public String getStockSymbol() {
		return stockSymbol;
	}
	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}
	public double getGain() {
		return gain;
	}
	public void setGain(double gain) {
		this.gain = gain;
	}
	public double getPercentage() {
		return percentage;
	}
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	
}
