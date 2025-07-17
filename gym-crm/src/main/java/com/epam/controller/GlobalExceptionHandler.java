package com.epam.controller;

import com.epam.dto.response.ErrorDetails;
import com.epam.dto.response.ErrorResponse;
import com.epam.dto.response.ResponseMessage;
import com.epam.exception.AccountBlockedException;
import com.epam.exception.AuthenticationException;
import com.epam.exception.EntityDoesNotExistException;
import com.epam.exception.InvalidPasswordException;
import com.epam.exception.InvalidatedTokenException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> ErrorResponse.message(
                        error.getDefaultMessage().toLowerCase()))
                .toList();
    }


    @ExceptionHandler(EntityDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseMessage handleEntityDoesNotExistException(EntityDoesNotExistException ex) {
        return new ResponseMessage(ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseMessage handleAuthenticationException(AuthenticationException e) {
        return new ResponseMessage(e.getMessage());
    }

    @ExceptionHandler(AccountBlockedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseMessage handleAccountBlockedException(AccountBlockedException e) {
        return new ResponseMessage(e.getMessage());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage handleInvalidPasswordException(InvalidPasswordException e) {
        return new ResponseMessage(e.getMessage());
    }

    @ExceptionHandler(InvalidatedTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage handleInvalidatedTokenException(InvalidatedTokenException e) {
        return new ResponseMessage(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException e)  {

        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException invalidFormatException) {
            return ErrorResponse.detailed(
                    ErrorDetails.builder()
                            .message(String.format("Cannot parse '%s'",  invalidFormatException.getValue().toString()))
                            .reason(invalidFormatException.getOriginalMessage() + " at " + invalidFormatException.getPath().get(0).getFieldName() )
                            .build()
            );
        }

        return ErrorResponse.detailed(
                ErrorDetails.builder()
                        .message("Malformed JSON request")
                        .reason(e.getMessage())
                        .build());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidFormat(InvalidFormatException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", ex.getMessage()));
    }

}
