package com.epam.dto.tranining;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

        @Future(message = "traningDate has to be in the future")
        LocalDate trainingDate,

        @NotNull(message = "trainingDurationMinutes cannot be empty")
        @Positive(message = "trainingDurationMinutes cannot be 0 or lower")
        @Min(message = "trainingDurationMinutes cannot be lower than 15 minutes", value = 15)
        @Max(message = "trainingDurationMinutes cannot be higher than 480 minutes (8 hours)", value = 480)
        Integer trainingDurationMinutes
) {}

