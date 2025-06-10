package com.epam.trainerworkloadservice.dto;

import com.epam.trainerworkloadservice.enums.ActionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TrainerWorkloadDto (

        @NotBlank(message = "Username must not be blank")
        String username,

        @NotBlank(message = "First name must not be blank")
        String firstname,

        @NotBlank(message = "Last name must not be blank")
        String lastname,

        boolean isActive,

        @NotNull(message = "Training date must not be null")
        LocalDate trainingDate,

        @NotNull
        @Positive(message = "Training duration must be a positive number")
        Integer trainingDuration,

        @NotNull(message = "Action type must not be null")
        ActionType actionType

) {}
