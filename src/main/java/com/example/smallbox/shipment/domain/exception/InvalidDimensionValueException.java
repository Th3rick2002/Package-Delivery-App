package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class InvalidDimensionValueException extends BadRequestException {
    public InvalidDimensionValueException(String dimension, String value) {
        super("INVALID_DIMENSION_" + dimension.toUpperCase(), "Invalid value for " + dimension + ": " + value + ". Must be greater than zero");
    }
}
