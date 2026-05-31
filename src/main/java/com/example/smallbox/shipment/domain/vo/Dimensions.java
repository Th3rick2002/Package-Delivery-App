package com.example.smallbox.shipment.domain.vo;

import com.example.smallbox.shipment.domain.exception.DimensionsRequiredException;
import com.example.smallbox.shipment.domain.exception.InvalidDimensionValueException;
import com.example.smallbox.shipment.domain.exception.UnsupportedDimensionUnitException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Dimensions(BigDecimal length, BigDecimal width, BigDecimal height, String unit) {
    private static final String SUPPORTED_UNIT = "CM";

    public Dimensions {
        if (length == null || width == null || height == null) {
            throw new DimensionsRequiredException();
        }
        if (length.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDimensionValueException("length", length.toString());
        }
        if (width.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDimensionValueException("width", width.toString());
        }
        if (height.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDimensionValueException("height", height.toString());
        }
        if (unit == null || unit.isBlank()) {
            throw new UnsupportedDimensionUnitException("null");
        }

        unit = unit.trim().toUpperCase();
        if (!SUPPORTED_UNIT.equals(unit)) {
            throw new UnsupportedDimensionUnitException(unit);
        }

        length = normalize(length);
        width = normalize(width);
        height = normalize(height);
    }

    public static Dimensions ofCm(BigDecimal length, BigDecimal width, BigDecimal height) {
        return new Dimensions(length, width, height, "CM");
    }

    public static Dimensions ofCm(double length, double width, double height) {
        return ofCm(BigDecimal.valueOf(length), BigDecimal.valueOf(width), BigDecimal.valueOf(height));
    }

    public BigDecimal getVolume() {
        return length.multiply(width).multiply(height).setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal normalize(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
