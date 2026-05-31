package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class UnsupportedWeightUnitException extends BadRequestException {
    public UnsupportedWeightUnitException(String unit) {
        super("UNSUPPORTED_WEIGHT_UNIT", "Unsupported weight unit: " + unit + ". Only KG is supported");
    }
}
