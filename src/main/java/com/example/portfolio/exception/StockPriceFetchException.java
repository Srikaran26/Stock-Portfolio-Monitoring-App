package com.example.portfolio.exception;

public class StockPriceFetchException extends RuntimeException {
    public StockPriceFetchException(String message) {
        super(message);
    }
}
