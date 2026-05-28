package com.example.smallbox.shipment.domain;

import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.LocationId;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.domain.enums.ShipmentStatus;
import com.example.smallbox.shipment.domain.vo.Price;
import com.example.smallbox.shipment.domain.vo.TrackingNumber;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record Shipment(
        Long shipmentId,
        TrackingNumber trackingNumber,
        UserId senderId,
        Recipient recipient,
        LocationId destinationCityId,
        String exactAddress,
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
        if (destinationCityId == null) throw new IllegalArgumentException("Destination city is required");
        if (exactAddress == null || exactAddress.isBlank()) throw new IllegalArgumentException("Exact address is required");
        if (originBranchId == null) throw new IllegalArgumentException("Origin branch is required");
        if (destinationBranchId == null) throw new IllegalArgumentException("Destination branch is required");
        if (status == null) throw new IllegalArgumentException("Status is required");
        if (packages == null || packages.isEmpty()) throw new IllegalArgumentException("At least one package is required");
        if (totalPrice == null) throw new IllegalArgumentException("Total price is required");
        if (createdAt == null) throw new IllegalArgumentException("Creation date is required");

        exactAddress = exactAddress.trim();
        packages = Collections.unmodifiableList(new ArrayList<>(packages));
    }

    public static Shipment create(
            UserId senderId,
            Recipient recipient,
            LocationId destinationCityId,
            String exactAddress,
            BranchID originBranchId,
            BranchID destinationBranchId,
            List<Package> packages,
            String trackingPrefix
    ) {
        Price totalPrice = calculateBasePrice(packages);

        return new Shipment(
                null,
                TrackingNumber.create(trackingPrefix),
                senderId,
                recipient,
                destinationCityId,
                exactAddress,
                originBranchId,
                destinationBranchId,
                ShipmentStatus.CREATED,
                packages,
                totalPrice,
                LocalDateTime.now()
        );
    }

    public static Shipment rehydrate(
            Long shipmentId,
            TrackingNumber trackingNumber,
            UserId senderId,
            Recipient recipient,
            LocationId destinationCityId,
            String exactAddress,
            BranchID originBranchId,
            BranchID destinationBranchId,
            ShipmentStatus status,
            List<Package> packages,
            Price totalPrice,
            LocalDateTime createdAt
    ) {
        return new Shipment(
                shipmentId,
                trackingNumber,
                senderId,
                recipient,
                destinationCityId,
                exactAddress,
                originBranchId,
                destinationBranchId,
                status,
                packages,
                totalPrice,
                createdAt
        );
    }

    private static Price calculateBasePrice(List<Package> packages) {
        if (packages == null || packages.isEmpty()) {
            throw new IllegalArgumentException("At least one package is required");
        }

        BigDecimal amount = packages.stream()
                .map(Package::calculateBaseCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Price.of(amount);
    }
}
