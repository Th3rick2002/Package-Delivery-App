package com.example.smallbox.shipment.domain;

import com.example.smallbox.shipment.domain.exception.ShipmentValidationException;
import com.example.smallbox.shipment.domain.vo.Dimensions;
import com.example.smallbox.shipment.domain.vo.Weight;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Package(
        Long packageId,
        String description,
        Weight weight,
        Dimensions dimensions
) {
    private static final BigDecimal HANDLING_FEE = new BigDecimal("2.50");
    private static final BigDecimal WEIGHT_RATE = new BigDecimal("1.25");
    private static final BigDecimal VOLUMETRIC_RATE = new BigDecimal("0.75");
    private static final BigDecimal VOLUME_DIVISOR = new BigDecimal("5000.00");

    public Package {
        if (description == null || description.isBlank()) {
            throw new ShipmentValidationException("PACKAGE_DESCRIPTION_REQUIRED", "Description is required");
        }
        if (weight == null) {
            throw new ShipmentValidationException("PACKAGE_WEIGHT_REQUIRED", "Weight is required");
        }
        if (dimensions == null) {
            throw new ShipmentValidationException("PACKAGE_DIMENSIONS_REQUIRED", "Dimensions are required");
        }
    }

    public static Package create(String description, Weight weight, Dimensions dimensions) {
        return new Package(null, description, weight, dimensions);
    }

    public BigDecimal calculateBaseCost() {
        BigDecimal volumetricWeight = dimensions.getVolume()
                .divide(VOLUME_DIVISOR, 2, RoundingMode.HALF_UP);

        return HANDLING_FEE
                .add(weight.value().multiply(WEIGHT_RATE))
                .add(volumetricWeight.multiply(VOLUMETRIC_RATE))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
