package com.portfolio.exception;

public class StockPriceFetchException extends RuntimeException {
    public StockPriceFetchException(String message) {
        super(message);
    }
}
