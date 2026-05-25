package com.example.smallbox.shared.domain;

public record Address(
        LocationId locationId,
        String streetLine,
        String zipCode
) {
    public Address {
        if (streetLine == null || streetLine.isBlank()) {
            throw new IllegalArgumentException("Street line cannot be empty");
        }
        if (zipCode == null || zipCode.isBlank()) {
            throw new IllegalArgumentException("Zip code cannot be empty");
        }
    }
}
