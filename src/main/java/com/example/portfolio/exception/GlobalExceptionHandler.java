package com.example.portfolio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex) {
	    String errorMessage = ex.getBindingResult().getFieldErrors()
	        .stream()
	        .map(error -> error.getField() + ": " + error.getDefaultMessage())
	        .findFirst()
	        .orElse("Validation error");
	    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}
}

