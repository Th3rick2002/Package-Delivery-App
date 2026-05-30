package com.example.smallbox.shipment.domain.vo;

import com.example.smallbox.shipment.domain.exception.ShipmentValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Weight(BigDecimal value, String unit) {
    private static final String SUPPORTED_UNIT = "KG";

    public Weight {
        if (value == null) {
            throw new ShipmentValidationException("WEIGHT_REQUIRED", "Weight is required");
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ShipmentValidationException("INVALID_WEIGHT", "Weight must be greater than zero");
        }
        if (unit == null || unit.isBlank()) {
            throw new ShipmentValidationException("WEIGHT_UNIT_REQUIRED", "Weight unit is required");
        }

        unit = unit.trim().toUpperCase();
        if (!SUPPORTED_UNIT.equals(unit)) {
            throw new ShipmentValidationException(
                    "UNSUPPORTED_WEIGHT_UNIT",
                    "Unsupported weight unit. Only KG is supported"
            );
        }

        value = value.setScale(2, RoundingMode.HALF_UP);
    }

    public static Weight ofKg(BigDecimal value) {
        return new Weight(value, "KG");
    }

    public static Weight ofKg(double value) {
        return ofKg(BigDecimal.valueOf(value));
    }
}
