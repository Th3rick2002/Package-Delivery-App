package com.example.smallbox.shared.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Standard API error response")
public record ApiErrorResponse(
        @Schema(description = "Unique error code for i18n", example = "USER_NOT_FOUND")
        String code,

        @Schema(description = "Detailed error message", example = "User with id 123 not found")
        String message,

        @Schema(description = "HTTP status code", example = "404")
        int status,

        @Schema(description = "Timestamp when the error occurred")
        LocalDateTime timestamp
) {
}
