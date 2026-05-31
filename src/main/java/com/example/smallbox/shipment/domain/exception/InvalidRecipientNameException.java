package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class InvalidRecipientNameException extends BadRequestException {
    public InvalidRecipientNameException(String field, String reason) {
        super("INVALID_RECIPIENT_NAME", field + " is invalid: " + reason);
    }
}
