package com.example.smallbox.shipment.domain.vo;

import com.example.smallbox.shipment.domain.exception.ShipmentValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Dimensions(BigDecimal length, BigDecimal width, BigDecimal height, String unit) {
    private static final String SUPPORTED_UNIT = "CM";

    public Dimensions {
        if (length == null || width == null || height == null) {
            throw new ShipmentValidationException("DIMENSIONS_REQUIRED", "Dimensions are required");
        }
        if (length.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ShipmentValidationException("INVALID_DIMENSION_LENGTH", "Length must be greater than zero");
        }
        if (width.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ShipmentValidationException("INVALID_DIMENSION_WIDTH", "Width must be greater than zero");
        }
        if (height.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ShipmentValidationException("INVALID_DIMENSION_HEIGHT", "Height must be greater than zero");
        }
        if (unit == null || unit.isBlank()) {
            throw new ShipmentValidationException("DIMENSION_UNIT_REQUIRED", "Dimension unit is required");
        }

        unit = unit.trim().toUpperCase();
        if (!SUPPORTED_UNIT.equals(unit)) {
            throw new ShipmentValidationException(
                    "UNSUPPORTED_DIMENSION_UNIT",
                    "Unsupported dimension unit. Only CM is supported"
            );
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
