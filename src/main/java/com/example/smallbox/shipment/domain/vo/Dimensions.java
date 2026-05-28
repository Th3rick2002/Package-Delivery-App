package com.example.smallbox.shipment.domain.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Dimensions(BigDecimal length, BigDecimal width, BigDecimal height, String unit) {
    public Dimensions {
        if (length == null || width == null || height == null) {
            throw new IllegalArgumentException("Dimensions are required");
        }
        if (length.compareTo(BigDecimal.ZERO) <= 0
                || width.compareTo(BigDecimal.ZERO) <= 0
                || height.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Dimensions must be greater than zero");
        }
        if (unit == null || unit.isBlank()) {
            throw new IllegalArgumentException("Unit is required");
        }

        length = normalize(length);
        width = normalize(width);
        height = normalize(height);
        unit = unit.trim().toUpperCase();
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
