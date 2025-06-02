package com.portfolio.exception;

public class AlertNotFoundException extends RuntimeException {
    public AlertNotFoundException(String message) {
        super(message);
    }
}