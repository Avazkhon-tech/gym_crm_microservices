package com.epam.dto.tranining;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TrainingCreateDto(

        @NotBlank
        String traineeUsername,

        @NotBlank
        String trainerUsername,

        @NotNull
        String trainingName,

        @Future
        LocalDate trainingDate,

        @NotNull @Positive
        Integer trainingDuration
) {}

