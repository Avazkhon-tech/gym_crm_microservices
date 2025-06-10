package com.epam.dto.trainer;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TrainerTrainingFilter(
        @NotBlank
        String username,
        LocalDate fromDate,
        LocalDate toDate,
        String traineeName
) {}
