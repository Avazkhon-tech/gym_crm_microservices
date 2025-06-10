package com.epam.dto.trainer;

import lombok.Builder;

import java.util.List;

@Builder
public record TrainerProfileDto(

        String firstname,

        String lastname,

        Boolean isActive,

        String specialization,

        List<TrainerTraineeDto> trainees
) {}

