package com.example.smallbox.shipment.domain.port;

import com.example.smallbox.shipment.domain.Shipment;
import com.example.smallbox.shipment.domain.vo.TrackingNumber;

import java.util.Optional;

public interface ShipmentRepository {
    Shipment save(Shipment shipment);
    Optional<Shipment> findById(Long id);
    Optional<Shipment> findByTrackingNumber(TrackingNumber trackingNumber);
}
