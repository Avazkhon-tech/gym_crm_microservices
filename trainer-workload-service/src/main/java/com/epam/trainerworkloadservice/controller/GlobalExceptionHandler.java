package com.epam.trainerworkloadservice.controller;

import com.epam.trainerworkloadservice.dto.response.ErrorDetails;
import com.epam.trainerworkloadservice.dto.response.ErrorResponse;
import com.epam.trainerworkloadservice.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(exception = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> com.epam.trainerworkloadservice.dto.response.ErrorResponse.message(
                        error.getDefaultMessage().toLowerCase()))
                .toList();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException e)  {

        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException invalidFormatException) {
            return ErrorResponse.detailed(
                    ErrorDetails.builder()
                            .message(String.format("Cannot parse '%s'",  invalidFormatException.getValue().toString()))
                            .reason("Invalid data format")
                            .build()
            );
        }

        return ErrorResponse.detailed(
                ErrorDetails.builder()
                        .message("Malformed JSON request")
                        .reason(e.getMessage())
                        .build());
    }
}
