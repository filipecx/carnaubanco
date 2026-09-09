package com.carnaubanco.userservice.exception;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.carnaubanco.userservice.dto.ErrorResponse;

@RestControllerAdvice 
public class GlobalExceptionHandler {
    
    @ExceptionHandler (UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(), ex.getMessage(), OffsetDateTime.now(ZoneOffset.UTC));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler (UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> userAlreadyExists(UserAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage(), OffsetDateTime.now(ZoneOffset.UTC));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(), 
            "Erro de validação nos dados enviados", 
            OffsetDateTime.now(ZoneOffset.UTC), 
            fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            
    }

    @ExceptionHandler (Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericExceptions(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Ocorreu um erro interno no servidor. Tente novamente mais tarde.",
            OffsetDateTime.now(ZoneOffset.UTC)
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
