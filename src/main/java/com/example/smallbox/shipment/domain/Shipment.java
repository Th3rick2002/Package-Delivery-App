package com.example.smallbox.shipment.domain;

import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.LocationId;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.domain.enums.ShipmentStatus;
import com.example.smallbox.shipment.domain.event.ShipmentStatusChangedEvent;
import com.example.smallbox.shipment.domain.exception.InvalidShipmentStatusException;
import com.example.smallbox.shipment.domain.exception.PackageRequiredException;
import com.example.smallbox.shipment.domain.exception.ShipmentFieldRequiredException;
import com.example.smallbox.shipment.domain.vo.Price;
import com.example.smallbox.shipment.domain.vo.TrackingNumber;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Shipment {

    private static final Map<ShipmentStatus, Set<ShipmentStatus>> VALID_TRANSITIONS = Map.of(
            ShipmentStatus.CREATED, Set.of(ShipmentStatus.RECEIVED_ORIGIN, ShipmentStatus.CANCELLED),
            ShipmentStatus.RECEIVED_ORIGIN, Set.of(ShipmentStatus.IN_TRANSIT, ShipmentStatus.CANCELLED),
            ShipmentStatus.IN_TRANSIT, Set.of(ShipmentStatus.RECEIVED_DESTINATION, ShipmentStatus.CANCELLED),
            ShipmentStatus.RECEIVED_DESTINATION, Set.of(ShipmentStatus.DELIVERED, ShipmentStatus.CANCELLED),
            ShipmentStatus.DELIVERED, Set.of(),
            ShipmentStatus.CANCELLED, Set.of()
    );

    private final Long shipmentId;
    private final TrackingNumber trackingNumber;
    private final UserId senderId;
    private final Recipient recipient;
    private final LocationId destinationCityId;
    private final String exactAddress;
    private final BranchID originBranchId;
    private final BranchID destinationBranchId;
    private ShipmentStatus status;
    private final List<Package> packages;
    private final Price totalPrice;
    private final LocalDateTime createdAt;

    private final List<ShipmentStatusChangedEvent> domainEvents = new ArrayList<>();

    private Shipment(
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
        if (trackingNumber == null) {
            throw new ShipmentFieldRequiredException("TRACKING_NUMBER");
        }
        if (senderId == null) {
            throw new ShipmentFieldRequiredException("SENDER");
        }
        if (recipient == null) {
            throw new ShipmentFieldRequiredException("RECIPIENT");
        }
        if (destinationCityId == null) {
            throw new ShipmentFieldRequiredException("DESTINATION_CITY");
        }
        if (exactAddress == null || exactAddress.isBlank()) {
            throw new ShipmentFieldRequiredException("EXACT_ADDRESS");
        }
        if (originBranchId == null) {
            throw new ShipmentFieldRequiredException("ORIGIN_BRANCH");
        }
        if (destinationBranchId == null) {
            throw new ShipmentFieldRequiredException("DESTINATION_BRANCH");
        }
        if (status == null) {
            throw new ShipmentFieldRequiredException("SHIPMENT_STATUS");
        }
        if (packages == null || packages.isEmpty()) {
            throw new PackageRequiredException();
        }
        if (totalPrice == null) {
            throw new ShipmentFieldRequiredException("TOTAL_PRICE");
        }
        if (createdAt == null) {
            throw new ShipmentFieldRequiredException("CREATION_DATE");
        }

        this.shipmentId = shipmentId;
        this.trackingNumber = trackingNumber;
        this.senderId = senderId;
        this.recipient = recipient;
        this.destinationCityId = destinationCityId;
        this.exactAddress = exactAddress.trim();
        this.originBranchId = originBranchId;
        this.destinationBranchId = destinationBranchId;
        this.status = status;
        this.packages = Collections.unmodifiableList(new ArrayList<>(packages));
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    // Record-compatible accessors for backward compatibility
    public Long shipmentId() { return shipmentId; }
    public TrackingNumber trackingNumber() { return trackingNumber; }
    public UserId senderId() { return senderId; }
    public Recipient recipient() { return recipient; }
    public LocationId destinationCityId() { return destinationCityId; }
    public String exactAddress() { return exactAddress; }
    public BranchID originBranchId() { return originBranchId; }
    public BranchID destinationBranchId() { return destinationBranchId; }
    public ShipmentStatus status() { return status; }
    public List<Package> packages() { return packages; }
    public Price totalPrice() { return totalPrice; }
    public LocalDateTime createdAt() { return createdAt; }

    // -------------------------------------------------------------------------
    // Behavior: state machine
    // -------------------------------------------------------------------------

    public void changeStatus(ShipmentStatus newStatus, UserId changedBy, String comments) {
        Set<ShipmentStatus> allowed = VALID_TRANSITIONS.getOrDefault(this.status, Set.of());
        if (!allowed.contains(newStatus)) {
            throw new InvalidShipmentStatusException(this.status, newStatus);
        }
        ShipmentStatus oldStatus = this.status;
        this.status = newStatus;
        domainEvents.add(new ShipmentStatusChangedEvent(shipmentId, oldStatus, newStatus, changedBy, comments, LocalDateTime.now()));
    }

    public void markAsReceivedAtOrigin(UserId changedBy) {
        changeStatus(ShipmentStatus.RECEIVED_ORIGIN, changedBy, null);
    }

    public void markAsInTransit(UserId changedBy) {
        changeStatus(ShipmentStatus.IN_TRANSIT, changedBy, null);
    }

    public void markAsReceivedAtDestination(UserId changedBy) {
        changeStatus(ShipmentStatus.RECEIVED_DESTINATION, changedBy, null);
    }

    public void deliver(UserId changedBy) {
        changeStatus(ShipmentStatus.DELIVERED, changedBy, null);
    }

    public void cancel(UserId changedBy, String reason) {
        changeStatus(ShipmentStatus.CANCELLED, changedBy, reason);
    }

    public List<ShipmentStatusChangedEvent> pullDomainEvents() {
        List<ShipmentStatusChangedEvent> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }

    // -------------------------------------------------------------------------
    // Factory methods
    // -------------------------------------------------------------------------

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
            throw new PackageRequiredException();
        }

        BigDecimal amount = packages.stream()
                .map(Package::calculateBaseCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Price.of(amount);
    }
}
