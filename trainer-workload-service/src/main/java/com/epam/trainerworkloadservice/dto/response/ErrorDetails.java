package com.epam.trainerworkloadservice.dto.response;

import lombok.Builder;

@Builder
public record ErrorDetails(
        String message,
        String reason
) {}