package com.example.smallbox.shared.application.dto;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        String code,
        String message,
        int status,
        LocalDateTime timestamp
) {
}
