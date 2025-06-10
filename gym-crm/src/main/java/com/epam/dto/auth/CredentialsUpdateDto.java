package com.epam.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record CredentialsUpdateDto(

        @NotBlank(message = "Username field cannot be empty")
        String username,

        @NotBlank(message = "Old password field cannot be empty")
        String oldPassword,

        @NotBlank(message = "Password field cannot be empty")
        String password
) {}
