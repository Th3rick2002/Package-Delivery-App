package com.example.smallbox.shipment.domain.port;

import com.example.smallbox.shipment.domain.Shipment;
import com.example.smallbox.shipment.domain.vo.TrackingNumber;

import java.util.Optional;
import java.util.UUID;

public interface ShipmentRepository {
    Shipment save(Shipment shipment);
    Optional<Shipment> findById(UUID id);
    Optional<Shipment> findByTrackingNumber(TrackingNumber trackingNumber);
}
