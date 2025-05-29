package com.example.portfolio.exception;

public class CsvImportException extends RuntimeException {
    public CsvImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
