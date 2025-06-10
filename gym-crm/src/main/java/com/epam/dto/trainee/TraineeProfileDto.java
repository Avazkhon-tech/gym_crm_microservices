package com.epam.dto.trainee;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record TraineeProfileDto(

        String firstname,

        String lastname,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate dateOfBirth,

        String address,

        Boolean isActive,

        List<TraineeTrainerDto> trainers

) {}