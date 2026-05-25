package com.example.smallbox.shared.domain;

public record LocationId(Integer cityId) {
    public LocationId {
        if (cityId == null || cityId <= 0) {
            throw new IllegalArgumentException("CityId must be a positive number");
        }
    }
}
