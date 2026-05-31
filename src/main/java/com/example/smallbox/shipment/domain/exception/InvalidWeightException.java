package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class InvalidWeightException extends BadRequestException {
    public InvalidWeightException() {
        super("INVALID_WEIGHT", "Weight must be greater than zero");
    }
}
