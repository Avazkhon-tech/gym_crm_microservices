package com.epam.trainerworkloadservice.exception;

public class TrainerAlreadyBusyException extends RuntimeException {
    public TrainerAlreadyBusyException(String trainerExceededDailyLimit) {
    }
}
