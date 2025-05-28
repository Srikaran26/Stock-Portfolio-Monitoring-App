package com.example.portfolio.exception;

public class FileGenerationException extends RuntimeException {
	public FileGenerationException() {
		super();
	}
	
	public FileGenerationException(String message) {
		super(message);
	}
	
	public FileGenerationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public FileGenerationException(Throwable cause) {
		super(cause);
	}

}
