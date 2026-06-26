package com.userInventory.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	//404 Not Found Error
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex){
		Map<String, Object>error=new LinkedHashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("status", 404);
		error.put("error", "Not Found");
		error.put("message", ex.getMessage());
		return ResponseEntity.status(404).body(error);
	}
	
	//400 Validation Error
	 @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<Object> handleValidation(
	            MethodArgumentNotValidException ex) {
	        Map<String, Object> error = new LinkedHashMap<>();
	        error.put("timestamp", LocalDateTime.now());
	        error.put("status",    400);
	        error.put("error",     "Validation Failed");

	        Map<String, String> fieldErrors = new HashMap<>();
	        ex.getBindingResult().getFieldErrors()
	          .forEach(e -> fieldErrors.put(
	              e.getField(),
	              e.getDefaultMessage()));
	        error.put("fieldErrors", fieldErrors);

	        return ResponseEntity.status(400).body(error);
	    }

	    // ── 400 Bad Request ───────────────────────────
	    @ExceptionHandler(RuntimeException.class)
	    public ResponseEntity<Object> handleRuntime(
	            RuntimeException ex) {
	        Map<String, Object> error = new LinkedHashMap<>();
	        error.put("timestamp", LocalDateTime.now());
	        error.put("status",    400);
	        error.put("error",     "Bad Request");
	        error.put("message",   ex.getMessage());
	        return ResponseEntity.status(400).body(error);
	    }

	    // ── 500 Server Error ──────────────────────────
	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<Object> handleAll(Exception ex) {
	        Map<String, Object> error = new LinkedHashMap<>();
	        error.put("timestamp", LocalDateTime.now());
	        error.put("status",    500);
	        error.put("error",     "Internal Server Error");
	        error.put("message", ex.getMessage());

	        ex.printStackTrace();
	        return ResponseEntity.status(500).body(error);
	    }
}
