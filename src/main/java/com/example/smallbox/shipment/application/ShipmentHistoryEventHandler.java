package com.example.smallbox.shipment.application;

import com.example.smallbox.shipment.domain.ShipmentHistory;
import com.example.smallbox.shipment.domain.event.ShipmentStatusChangedEvent;
import com.example.smallbox.shipment.domain.port.ShipmentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ShipmentHistoryEventHandler {

    private final ShipmentHistoryRepository historyRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(ShipmentStatusChangedEvent event) {
        ShipmentHistory history = ShipmentHistory.create(
                event.shipmentId(),
                event.newStatus(),
                event.changedBy(),
                event.comments()
        );
        historyRepository.save(history);
    }
}
