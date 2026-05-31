package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class UnsupportedDimensionUnitException extends BadRequestException {
    public UnsupportedDimensionUnitException(String unit) {
        super("UNSUPPORTED_DIMENSION_UNIT", "Unsupported dimension unit: " + unit + ". Only CM is supported");
    }
}
