package com.example.portfolio.service;

public interface StockPriceService{
	
	double getPrice(String stockSymbol);
	double fetchLivePrice(String stockSymbol);
}