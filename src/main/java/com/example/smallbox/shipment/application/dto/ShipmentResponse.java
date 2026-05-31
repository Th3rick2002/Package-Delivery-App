package com.example.smallbox.shipment.application.dto;

import com.example.smallbox.shipment.domain.Package;
import com.example.smallbox.shipment.domain.Recipient;
import com.example.smallbox.shipment.domain.Shipment;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Shipment details response")
public record ShipmentResponse(
        @Schema(description = "Unique tracking number for the shipment", example = "SB-123456789")
        String trackingNumber,

        @Schema(description = "Current status of the shipment", example = "PENDING")
        String status,

        @Schema(description = "Total cost of the shipment", example = "25.50")
        BigDecimal totalPrice,

        @Schema(description = "Currency of the price", example = "USD")
        String currency,

        @Schema(description = "ID of the sender user")
        UUID senderId,

        @Schema(description = "Recipient information")
        RecipientInfo recipient,

        @Schema(description = "ID of the destination city")
        Integer destinationCityId,

        @Schema(description = "Exact delivery address")
        String exactAddress,

        @Schema(description = "ID of the origin branch")
        Integer originBranchId,

        @Schema(description = "ID of the destination branch")
        Integer destinationBranchId,

        @Schema(description = "List of packages in the shipment")
        List<PackageInfo> packages,

        @Schema(description = "Timestamp when the shipment was created")
        LocalDateTime createdAt
) {
    @Schema(description = "Recipient information")
    public record RecipientInfo(
            @Schema(description = "Recipient's full name")
            String fullName,

            @Schema(description = "Recipient's phone number")
            String phone,

            @Schema(description = "Recipient's email")
            String email
    ) {}

    @Schema(description = "Package details")
    public record PackageInfo(
            @Schema(description = "Description of contents")
            String description,

            @Schema(description = "Weight")
            BigDecimal weight,

            @Schema(description = "Weight unit")
            String weightUnit,

            @Schema(description = "Length")
            BigDecimal length,

            @Schema(description = "Width")
            BigDecimal width,

            @Schema(description = "Height")
            BigDecimal height,

            @Schema(description = "Dimensions unit")
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
