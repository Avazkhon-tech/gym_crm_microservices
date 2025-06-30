package com.epam.exception;

public class InvalidatedTokenException extends RuntimeException {
    public InvalidatedTokenException(String message) {
        super(message);
    }
}