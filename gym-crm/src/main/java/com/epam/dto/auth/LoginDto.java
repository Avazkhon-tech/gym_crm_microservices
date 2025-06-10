package com.epam.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginDto(

        @NotBlank(message = "Username field cannot be empty")
        String username,
        @NotBlank(message = "Password field cannot be empty")
        String password
) {}
