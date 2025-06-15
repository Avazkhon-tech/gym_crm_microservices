package com.epam.dto.training;

import com.epam.enums.ActionType;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
public record TrainerWorkloadDto(

        String username,
        String firstname,
        String lastname,
        boolean isActive,
        LocalDate trainingDate,
        Integer trainingDurationMinutes,
        ActionType actionType
) implements Serializable {}
