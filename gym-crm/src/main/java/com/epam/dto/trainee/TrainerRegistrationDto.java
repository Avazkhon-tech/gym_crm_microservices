package com.epam.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TrainerRegistrationDto(

        @NotBlank(message = "Firstname field cannot be empty")
        String firstname,

        @NotBlank(message = "Lastname field cannot be empty")
        String lastname,

        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        String address
) {}
