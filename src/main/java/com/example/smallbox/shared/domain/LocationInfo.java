package com.example.smallbox.shared.domain;

public record LocationInfo(
        String cityName,
        String countryName,
        String deliveryZoneCode
) {
    public LocationInfo {
        if (cityName == null || cityName.isBlank()) throw new IllegalArgumentException("City name is required");
    }
}
