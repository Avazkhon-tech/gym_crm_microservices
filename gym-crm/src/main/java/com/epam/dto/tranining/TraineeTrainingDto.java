package com.epam.dto.tranining;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TraineeTrainingDto(

        String trainingName,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate trainingDate,

        String trainingType,

        Integer trainingDuration,

        String trainerName

) {}

