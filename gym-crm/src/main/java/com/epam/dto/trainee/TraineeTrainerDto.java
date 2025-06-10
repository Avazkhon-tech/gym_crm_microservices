package com.epam.dto.trainee;

import lombok.Builder;

@Builder
public record TraineeTrainerDto(

        String username,

        String firstname,

        String lastname,

        String specialization
) {}

