package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class RecipientFieldRequiredException extends BadRequestException {
    public RecipientFieldRequiredException(String field) {
        super("RECIPIENT_" + field.toUpperCase() + "_REQUIRED", field + " is required for the recipient");
    }
}
