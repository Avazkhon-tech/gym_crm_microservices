package com.epam.dto.tranining;

import jakarta.validation.constraints.Future;
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

        @NotNull(message = "trainingDuration cannot be empty")
        @Positive(message = "trainingDuration cannot be 0 or lower")
        Integer trainingDuration
) {}

