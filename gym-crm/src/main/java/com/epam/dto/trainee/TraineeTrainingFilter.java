package com.epam.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TraineeTrainingFilter(
        @NotBlank
        String username,
        LocalDate fromDate,
        LocalDate toDate,
        String trainerName,
        String trainingType
) {
}
