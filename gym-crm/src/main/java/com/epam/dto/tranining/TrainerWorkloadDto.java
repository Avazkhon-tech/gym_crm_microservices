package com.epam.dto.tranining;

import com.epam.enums.ActionType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TrainerWorkloadDto(

        String username,
        String firstname,
        String lastname,
        boolean isActive,
        LocalDate trainingDate,
        Integer trainingDuration,
        ActionType actionType
) {}
