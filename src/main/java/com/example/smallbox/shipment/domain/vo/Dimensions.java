package com.example.smallbox.shipment.domain.vo;

public record Dimensions(double length, double width, double height, String unit) {
    public Dimensions {
        if (length <= 0 || width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be greater than zero");
        }
        if (unit == null || unit.isBlank()) {
            throw new IllegalArgumentException("Unit is required");
        }
    }

    public static Dimensions ofCm(double length, double width, double height) {
        return new Dimensions(length, width, height, "CM");
    }

    public double getVolume() {
        return length * width * height;
    }
}
