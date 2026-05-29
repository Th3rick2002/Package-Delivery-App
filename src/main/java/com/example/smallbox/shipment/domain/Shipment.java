package com.example.smallbox.shipment.domain;

import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.LocationId;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.domain.enums.ShipmentStatus;
import com.example.smallbox.shipment.domain.exception.ShipmentValidationException;
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
        if (trackingNumber == null) {
            throw new ShipmentValidationException("TRACKING_NUMBER_REQUIRED", "Tracking number is required");
        }
        if (senderId == null) {
            throw new ShipmentValidationException("SENDER_REQUIRED", "Sender ID is required");
        }
        if (recipient == null) {
            throw new ShipmentValidationException("RECIPIENT_REQUIRED", "Recipient is required");
        }
        if (destinationCityId == null) {
            throw new ShipmentValidationException("DESTINATION_CITY_REQUIRED", "Destination city is required");
        }
        if (exactAddress == null || exactAddress.isBlank()) {
            throw new ShipmentValidationException("EXACT_ADDRESS_REQUIRED", "Exact address is required");
        }
        if (originBranchId == null) {
            throw new ShipmentValidationException("ORIGIN_BRANCH_REQUIRED", "Origin branch is required");
        }
        if (destinationBranchId == null) {
            throw new ShipmentValidationException("DESTINATION_BRANCH_REQUIRED", "Destination branch is required");
        }
        if (status == null) {
            throw new ShipmentValidationException("SHIPMENT_STATUS_REQUIRED", "Status is required");
        }
        if (packages == null || packages.isEmpty()) {
            throw new ShipmentValidationException("PACKAGE_REQUIRED", "At least one package is required");
        }
        if (totalPrice == null) {
            throw new ShipmentValidationException("TOTAL_PRICE_REQUIRED", "Total price is required");
        }
        if (createdAt == null) {
            throw new ShipmentValidationException("CREATION_DATE_REQUIRED", "Creation date is required");
        }

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
            throw new ShipmentValidationException("PACKAGE_REQUIRED", "At least one package is required");
        }

        BigDecimal amount = packages.stream()
                .map(Package::calculateBaseCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Price.of(amount);
    }
}
