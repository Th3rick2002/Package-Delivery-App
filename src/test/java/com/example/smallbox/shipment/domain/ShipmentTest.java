package com.example.smallbox.shipment.domain;

import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.Email;
import com.example.smallbox.shared.domain.LocationId;
import com.example.smallbox.shared.domain.Phone;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.domain.enums.ShipmentStatus;
import com.example.smallbox.shipment.domain.exception.ShipmentValidationException;
import com.example.smallbox.shipment.domain.vo.Dimensions;
import com.example.smallbox.shipment.domain.vo.Weight;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void weightRejectsUnsupportedUnitsWithSemanticCode() {
        ShipmentValidationException exception = assertThrows(
                ShipmentValidationException.class,
                () -> new Weight(new BigDecimal("1.00"), "lb")
        );

        assertEquals("UNSUPPORTED_WEIGHT_UNIT", exception.getErrorCode());
    }

    @Test
    void dimensionsRejectUnsupportedUnitsWithSemanticCode() {
        ShipmentValidationException exception = assertThrows(
                ShipmentValidationException.class,
                () -> new Dimensions(
                        new BigDecimal("10.00"),
                        new BigDecimal("10.00"),
                        new BigDecimal("10.00"),
                        "meter"
                )
        );

        assertEquals("UNSUPPORTED_DIMENSION_UNIT", exception.getErrorCode());
    }

    @Test
    void unitsAreNormalizedWhenSupported() {
        Weight weight = new Weight(new BigDecimal("2.00"), " kg ");
        Dimensions dimensions = new Dimensions(
                new BigDecimal("10.00"),
                new BigDecimal("20.00"),
                new BigDecimal("30.00"),
                " cm "
        );

        assertEquals("KG", weight.unit());
        assertEquals("CM", dimensions.unit());
    }

    @Test
    void weightRejectsZeroWithSemanticCode() {
        ShipmentValidationException exception = assertThrows(
                ShipmentValidationException.class,
                () -> Weight.ofKg(BigDecimal.ZERO)
        );

        assertEquals("INVALID_WEIGHT", exception.getErrorCode());
    }

    @Test
    void dimensionsRejectInvalidLengthWithSemanticCode() {
        ShipmentValidationException exception = assertThrows(
                ShipmentValidationException.class,
                () -> Dimensions.ofCm(BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE)
        );

        assertEquals("INVALID_DIMENSION_LENGTH", exception.getErrorCode());
    }

    @Test
    void packageRejectsBlankDescriptionWithSemanticCode() {
        ShipmentValidationException exception = assertThrows(
                ShipmentValidationException.class,
                () -> Package.create(
                        " ",
                        Weight.ofKg(new BigDecimal("1.00")),
                        Dimensions.ofCm(10, 10, 10)
                )
        );

        assertEquals("PACKAGE_DESCRIPTION_REQUIRED", exception.getErrorCode());
    }

    @Test
    void shipmentRejectsBlankAddressWithSemanticCode() {
        ShipmentValidationException exception = assertThrows(
                ShipmentValidationException.class,
                () -> Shipment.create(
                        UserId.generate(),
                        validRecipient(),
                        new LocationId(28),
                        " ",
                        new BranchID(1),
                        new BranchID(2),
                        validPackages(),
                        "SB"
                )
        );

        assertEquals("EXACT_ADDRESS_REQUIRED", exception.getErrorCode());
    }

    private Recipient validRecipient() {
        return Recipient.create(
                "Carlos",
                null,
                "Ramirez",
                null,
                new Phone("77777777"),
                new Email("carlos@example.com")
        );
    }

    private List<Package> validPackages() {
        return List.of(
                Package.create("Books", Weight.ofKg(new BigDecimal("2.00")), Dimensions.ofCm(20, 10, 5))
        );
    }
}
