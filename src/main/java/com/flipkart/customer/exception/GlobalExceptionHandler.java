package com.flipkart.customer.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.flipkart.customer.response.ValidationErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler
{

	@ExceptionHandler(DuplicateEmailException.class)
	public ResponseEntity<Map<String, Object>> handleDuplicateEmail(DuplicateEmailException exception)
	{

		Map<String, Object> response = new LinkedHashMap<>();

		response.put("timestamp", LocalDateTime.now());
		response.put("status", HttpStatus.CONFLICT.value());
		response.put("error", "Conflict");
		response.put("message", exception.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	@ExceptionHandler(DuplicateMobileException.class)
	public ResponseEntity<Map<String, Object>> handleDuplicateMobile(DuplicateMobileException exception)
	{

		Map<String, Object> response = new LinkedHashMap<>();

		response.put("timestamp", LocalDateTime.now());
		response.put("status", HttpStatus.CONFLICT.value());
		response.put("error", "Conflict");
		response.put("message", exception.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	/*
	 * Handles Bean Validation failures triggered by @Valid on the controller
	 * request body.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException exception)
	{

		Map<String, String> errors = new LinkedHashMap<>();

		exception.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		ValidationErrorResponse response = new ValidationErrorResponse();

		response.setTimestamp(LocalDateTime.now());
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setError("Bad Request");
		response.setMessage("Validation failed");
		response.setErrors(errors);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}