package com.epam.dto.response;

import lombok.Builder;

@Builder
public record ResponseDto<T> (
    Integer code,
    boolean success,
    String message,
    T data
) {}