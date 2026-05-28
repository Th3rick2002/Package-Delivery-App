package com.example.smallbox.shipment.domain.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Weight(BigDecimal value, String unit) {
    public Weight {
        if (value == null) {
            throw new IllegalArgumentException("Weight is required");
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Weight must be greater than zero");
        }
        if (unit == null || unit.isBlank()) {
            throw new IllegalArgumentException("Unit is required");
        }

        value = value.setScale(2, RoundingMode.HALF_UP);
        unit = unit.trim().toUpperCase();
    }

    public static Weight ofKg(BigDecimal value) {
        return new Weight(value, "KG");
    }

    public static Weight ofKg(double value) {
        return ofKg(BigDecimal.valueOf(value));
    }
}
