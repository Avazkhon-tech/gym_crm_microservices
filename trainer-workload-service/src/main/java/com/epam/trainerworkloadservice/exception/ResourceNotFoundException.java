package com.epam.trainerworkloadservice.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException of(String message, String resourceName) {
        return new ResourceNotFoundException(message + ": " + resourceName);
    }
}