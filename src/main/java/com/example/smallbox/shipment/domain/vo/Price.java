package com.example.smallbox.shipment.domain.vo;

import com.example.smallbox.shipment.domain.exception.CurrencyMismatchException;
import com.example.smallbox.shipment.domain.exception.InvalidPriceException;
import com.example.smallbox.shipment.domain.exception.ShipmentFieldRequiredException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Price(BigDecimal amount, String currency) {
    public Price {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPriceException(amount);
        }

        if (currency == null || currency.isBlank()) {
            throw new ShipmentFieldRequiredException("CURRENCY");
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Price of(BigDecimal amount) {
        return new Price(amount, "USD");
    }

    public Price plus(Price other) {
        validateSameCurrency(other);
        return new Price(this.amount.add(other.amount), this.currency);
    }

    public Price minus(Price other) {
        validateSameCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);
        return new Price(result, this.currency);
    }

    public Price multiply(double factor) {
        BigDecimal result = this.amount.multiply(BigDecimal.valueOf(factor));
        return new Price(result, this.currency);
    }

    private void validateSameCurrency(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new CurrencyMismatchException(this.currency, other.currency);
        }
    }
}
