package com.example.smallbox.shipment.domain;

import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.domain.enums.ShipmentStatus;
import com.example.smallbox.shipment.domain.vo.Price;
import com.example.smallbox.shipment.domain.vo.TrackingNumber;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record Shipment(
        UUID shipmentId,
        TrackingNumber trackingNumber,
        UserId senderId,
        Recipient recipient,
        BranchID originBranchId,
        BranchID destinationBranchId,
        ShipmentStatus status,
        List<Package> packages,
        Price totalPrice,
        LocalDateTime createdAt
) {
    public Shipment {
        if (trackingNumber == null) throw new IllegalArgumentException("Tracking number is required");
        if (senderId == null) throw new IllegalArgumentException("Sender ID is required");
        if (recipient == null) throw new IllegalArgumentException("Recipient is required");
        if (originBranchId == null) throw new IllegalArgumentException("Origin branch is required");
        if (destinationBranchId == null) throw new IllegalArgumentException("Destination branch is required");
        if (status == null) throw new IllegalArgumentException("Status is required");
        if (packages == null || packages.isEmpty()) throw new IllegalArgumentException("At least one package is required");
        if (totalPrice == null) throw new IllegalArgumentException("Total price is required");
        if (createdAt == null) throw new IllegalArgumentException("Creation date is required");
    }

    public static Shipment create(
            UserId senderId,
            Recipient recipient,
            BranchID originBranchId,
            BranchID destinationBranchId,
            List<Package> packages,
            Price totalPrice,
            String trackingPrefix
    ) {
        return new Shipment(
                null,
                TrackingNumber.create(trackingPrefix),
                senderId,
                recipient,
                originBranchId,
                destinationBranchId,
                ShipmentStatus.CREATED,
                packages,
                totalPrice,
                LocalDateTime.now()
        );
    }
}
