package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

import java.math.BigDecimal;

public class InvalidPriceException extends BadRequestException {
    public InvalidPriceException(BigDecimal amount) {
        super("INVALID_PRICE_AMOUNT", "Price amount must be greater than zero: " + amount);
    }
}
