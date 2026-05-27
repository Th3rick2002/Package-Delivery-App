package com.example.smallbox.shipment.domain.vo;

public record Weight(double value, String unit) {
    public Weight {
        if (value <= 0) {
            throw new IllegalArgumentException("Weight must be greater than zero");
        }
        if (unit == null || unit.isBlank()) {
            throw new IllegalArgumentException("Unit is required");
        }
    }

    public static Weight ofKg(double value) {
        return new Weight(value, "KG");
    }
}
