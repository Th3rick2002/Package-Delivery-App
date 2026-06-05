package com.example.smallbox.shipment.domain.port;

import com.example.smallbox.shipment.domain.ShipmentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShipmentHistoryRepository {
    ShipmentHistory save(ShipmentHistory history);
    Page<ShipmentHistory> findByShipmentId(Long shipmentId, Pageable pageable);
}

