package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class DimensionsRequiredException extends BadRequestException {
    public DimensionsRequiredException() {
        super("DIMENSIONS_REQUIRED", "Package dimensions are required");
    }
}
