package com.epam.dto.training;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TrainerTrainingDto(

        String trainingName,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate trainingDate,

        String trainingType,

        Integer trainingDurationMinutes,

        String traineeName

) {}

