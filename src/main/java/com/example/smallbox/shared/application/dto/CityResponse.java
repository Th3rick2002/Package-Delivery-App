package com.example.smallbox.shared.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "City information response")
public record CityResponse(
        @Schema(description = "City unique identifier", example = "10")
        Integer id,

        @Schema(description = "City name", example = "Bogotá")
        String name,

        @Schema(description = "Country name", example = "Colombia")
        String countryName
) {
}
