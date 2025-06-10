package com.epam.dto.response;

public record ErrorResponse(Object error) {

    public static ErrorResponse detailed(ErrorDetails error) {
        return new ErrorResponse(error);
    }

    public static ErrorResponse message(String message) {
        return new ErrorResponse(message);
    }

}

