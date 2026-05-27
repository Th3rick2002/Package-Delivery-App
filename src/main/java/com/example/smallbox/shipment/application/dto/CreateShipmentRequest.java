package com.example.smallbox.shipment.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateShipmentRequest(
        UUID senderId,
        RecipientRequest recipient,
        Integer originBranchId,
        Integer destinationBranchId,
        List<PackageRequest> packages,
        BigDecimal totalAmount,
        String currency
) {
    public record RecipientRequest(
            String firstName,
            String secondName,
            String lastName,
            String secondLastName,
            String phone,
            String email
    ) {}

    public record PackageRequest(
            String description,
            double weight,
            String weightUnit,
            double length,
            double width,
            double height,
            String dimensionsUnit
    ) {}
}
