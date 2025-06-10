package com.epam.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TraineeProfileUpdateDto(

        @NotBlank(message = "Firstname field cannot be empty")
        String firstname,

        @NotBlank(message = "Lastname field cannot be empty")
        String lastname,

        @Past(message = "Date of birth has to be in the past")
        LocalDate dateOfBirth,

        String address,

        @NotNull(message = "isActive cannot be empty")
        Boolean isActive
) {}
