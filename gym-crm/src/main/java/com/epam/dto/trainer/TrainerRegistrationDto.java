package com.epam.dto.trainer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record TrainerRegistrationDto(

        @NotBlank(message = "Firstname field cannot be empty")
        String firstname,

        @NotBlank(message = "Lastname field cannot be empty")
        String lastname,

        @NotNull(message = "Specialization field cannot be empty")
        @Positive(message = "Specialization id must be higher than 0")
        Integer specializationId
) {
}
