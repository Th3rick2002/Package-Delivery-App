package com.example.smallbox.shipment.application;

import com.example.smallbox.shipment.domain.Shipment;
import com.example.smallbox.shipment.domain.ShipmentHistory;
import com.example.smallbox.shipment.domain.exception.ShipmentNotFoundException;
import com.example.smallbox.shipment.domain.port.ShipmentHistoryRepository;
import com.example.smallbox.shipment.domain.port.ShipmentRepository;
import com.example.smallbox.shipment.domain.vo.TrackingNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetShipmentHistoryUseCase {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentHistoryRepository historyRepository;

    @Transactional(readOnly = true)
    public List<ShipmentHistory> execute(String trackingNumber) {
        Shipment shipment = shipmentRepository.findByTrackingNumber(new TrackingNumber(trackingNumber))
                .orElseThrow(() -> new ShipmentNotFoundException(trackingNumber));
        return historyRepository.findByShipmentId(shipment.shipmentId());
    }
}
