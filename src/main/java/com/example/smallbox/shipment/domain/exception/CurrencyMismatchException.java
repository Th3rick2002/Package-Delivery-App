package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class CurrencyMismatchException extends BadRequestException {
    public CurrencyMismatchException(String currency1, String currency2) {
        super("CURRENCY_MISMATCH", "Cannot operate with different currencies: " + currency1 + " and " + currency2);
    }
}
