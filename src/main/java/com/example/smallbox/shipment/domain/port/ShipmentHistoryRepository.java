package com.example.smallbox.shipment.domain.port;

import com.example.smallbox.shipment.domain.ShipmentHistory;

import java.util.List;

public interface ShipmentHistoryRepository {
    ShipmentHistory save(ShipmentHistory history);
    List<ShipmentHistory> findByShipmentId(Long shipmentId);
}
