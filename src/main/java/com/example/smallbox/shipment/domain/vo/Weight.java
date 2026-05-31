package com.example.smallbox.shipment.domain.vo;

import com.example.smallbox.shipment.domain.exception.InvalidWeightException;
import com.example.smallbox.shipment.domain.exception.UnsupportedWeightUnitException;
import com.example.smallbox.shipment.domain.exception.WeightRequiredException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Weight(BigDecimal value, String unit) {
    private static final String SUPPORTED_UNIT = "KG";

    public Weight {
        if (value == null) {
            throw new WeightRequiredException();
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidWeightException();
        }
        if (unit == null || unit.isBlank()) {
            throw new UnsupportedWeightUnitException("null");
        }

        unit = unit.trim().toUpperCase();
        if (!SUPPORTED_UNIT.equals(unit)) {
            throw new UnsupportedWeightUnitException(unit);
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
