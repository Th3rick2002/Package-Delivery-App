package com.example.smallbox.shipment.domain;

import com.example.smallbox.shipment.domain.vo.Dimensions;
import com.example.smallbox.shipment.domain.vo.Weight;

import java.util.UUID;

public record Package(
        UUID packageId,
        String description,
        Weight weight,
        Dimensions dimensions
) {
    public Package {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (weight == null) {
            throw new IllegalArgumentException("Weight is required");
        }
        if (dimensions == null) {
            throw new IllegalArgumentException("Dimensions are required");
        }
    }

    public static Package create(String description, Weight weight, Dimensions dimensions) {
        return new Package(null, description, weight, dimensions);
    }
}
