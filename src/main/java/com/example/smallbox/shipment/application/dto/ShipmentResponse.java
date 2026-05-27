package com.example.smallbox.shipment.application.dto;

import com.example.smallbox.shipment.domain.Package;
import com.example.smallbox.shipment.domain.Recipient;
import com.example.smallbox.shipment.domain.Shipment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ShipmentResponse(
        String trackingNumber,
        String status,
        BigDecimal totalPrice,
        String currency,
        UUID senderId,
        RecipientInfo recipient,
        Integer destinationCityId,
        String exactAddress,
        Integer originBranchId,
        Integer destinationBranchId,
        List<PackageInfo> packages,
        LocalDateTime createdAt
) {
    public record RecipientInfo(
            String fullName,
            String phone,
            String email
    ) {}

    public record PackageInfo(
            String description,
            BigDecimal weight,
            String weightUnit,
            BigDecimal length,
            BigDecimal width,
            BigDecimal height,
            String dimensionsUnit
    ) {}

    public static ShipmentResponse from(Shipment shipment) {
        return new ShipmentResponse(
                shipment.trackingNumber().value(),
                shipment.status().name(),
                shipment.totalPrice().amount(),
                shipment.totalPrice().currency(),
                shipment.senderId().uuid(),
                toRecipientInfo(shipment.recipient()),
                shipment.destinationCityId().cityId(),
                shipment.exactAddress(),
                shipment.originBranchId().id(),
                shipment.destinationBranchId().id(),
                shipment.packages().stream().map(ShipmentResponse::toPackageInfo).toList(),
                shipment.createdAt()
        );
    }

    private static RecipientInfo toRecipientInfo(Recipient recipient) {
        return new RecipientInfo(
                recipient.getFullName(),
                recipient.phone().value(),
                recipient.email().value()
        );
    }

    private static PackageInfo toPackageInfo(Package pkg) {
        return new PackageInfo(
                pkg.description(),
                pkg.weight().value(),
                pkg.weight().unit(),
                pkg.dimensions().length(),
                pkg.dimensions().width(),
                pkg.dimensions().height(),
                pkg.dimensions().unit()
        );
    }
}
