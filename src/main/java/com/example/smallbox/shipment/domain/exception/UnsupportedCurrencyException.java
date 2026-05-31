package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class UnsupportedCurrencyException extends BadRequestException {
    public UnsupportedCurrencyException(String currency) {
        super("UNSUPPORTED_CURRENCY", "Unsupported currency: " + currency + ". Only USD is supported");
    }
}
