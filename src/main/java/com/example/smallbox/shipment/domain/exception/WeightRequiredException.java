package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class WeightRequiredException extends BadRequestException {
    public WeightRequiredException() {
        super("WEIGHT_REQUIRED", "Package weight is required");
    }
}
