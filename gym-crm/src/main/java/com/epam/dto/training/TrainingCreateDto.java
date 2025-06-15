package com.epam.dto.training;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TrainingCreateDto(

        @NotBlank(message = "traineeUsername cannot be empty")
        String traineeUsername,

        @NotBlank(message = "trainerUsername cannot be empty")
        String trainerUsername,

        @NotBlank(message = "training name cannot be empty")
        String trainingName,

        @Future(message = "trainingDate has to be in the future")
        LocalDate trainingDate,

        @NotNull(message = "trainingDurationMinutes cannot be empty")
        @Min(message = "trainingDurationMinutes cannot be lower than 15 minutes", value = 15)
        @Max(message = "trainingDurationMinutes cannot be higher than 480 minutes (8 hours)", value = 480)
        Integer trainingDurationMinutes
) {}
