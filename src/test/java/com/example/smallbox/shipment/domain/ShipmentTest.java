package com.example.smallbox.shipment.domain;

import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.Email;
import com.example.smallbox.shared.domain.LocationId;
import com.example.smallbox.shared.domain.Phone;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.domain.enums.ShipmentStatus;
import com.example.smallbox.shipment.domain.vo.Dimensions;
import com.example.smallbox.shipment.domain.vo.Weight;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ShipmentTest {

    @Test
    void createInitializesShipmentInvariants() {
        Recipient recipient = Recipient.create(
                "Carlos",
                null,
                "Ramirez",
                null,
                new Phone("77777777"),
                new Email("carlos@example.com")
        );

        Shipment shipment = Shipment.create(
                UserId.generate(),
                recipient,
                new LocationId(28),
                "Colonia Centro #123",
                new BranchID(1),
                new BranchID(2),
                List.of(
                        Package.create("Books", Weight.ofKg(new BigDecimal("2.00")), Dimensions.ofCm(20, 10, 5)),
                        Package.create("Laptop", Weight.ofKg(new BigDecimal("3.50")), Dimensions.ofCm(35, 25, 8))
                ),
                "SB"
        );

        assertEquals(ShipmentStatus.CREATED, shipment.status());
        assertEquals(2, shipment.packages().size());
        assertEquals(new BigDecimal("13.08"), shipment.totalPrice().amount());
        assertNotNull(shipment.trackingNumber());
        assertNotNull(shipment.createdAt());
    }
}
