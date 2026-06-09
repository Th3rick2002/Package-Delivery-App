package com.example.smallbox.shipment.domain.service;

import com.example.smallbox.branch.domain.Branch;
import com.example.smallbox.branch.domain.exception.BranchInactiveException;
import com.example.smallbox.branch.domain.exception.BranchNotFoundException;
import com.example.smallbox.branch.domain.port.BranchRepository;
import com.example.smallbox.shared.domain.*;
import com.example.smallbox.shipment.domain.Package;
import com.example.smallbox.shipment.domain.Recipient;
import com.example.smallbox.shipment.domain.Shipment;
import com.example.smallbox.shipment.domain.vo.Dimensions;
import com.example.smallbox.shipment.domain.vo.Weight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShipmentDomainServiceTest {

    private BranchRepository branchRepository;
    private ShipmentDomainService shipmentDomainService;

    @BeforeEach
    void setUp() {
        branchRepository = mock(BranchRepository.class);
        shipmentDomainService = new ShipmentDomainService(branchRepository);
    }

    @Test
    void createBranchToBranchShipment_SuccessfullyCreatesShipment() {
        UserId senderId = UserId.generate();
        Recipient recipient = Recipient.create("Jane", null, "Doe", null, new Phone("88888888"), new Email("jane@example.com"));
        List<Package> packages = List.of(Package.create("Books", Weight.ofKg(2.0), Dimensions.ofCm(10, 10, 10)));

        Branch originBranch = Branch.builder()
                .id(new BranchID(1))
                .name("ORIGIN")
                .city(new LocationId(10))
                .phone(new Phone("88888888"))
                .build();

        Branch destinationBranch = Branch.builder()
                .id(new BranchID(2))
                .name("DESTINATION")
                .city(new LocationId(20))
                .phone(new Phone("99999999"))
                .build();

        when(branchRepository.findById(1)).thenReturn(Optional.of(originBranch));
        when(branchRepository.findById(2)).thenReturn(Optional.of(destinationBranch));

        Shipment shipment = shipmentDomainService.createBranchToBranchShipment(
                senderId, recipient, 1, 2, packages, "SBX"
        );

        assertNotNull(shipment);
        assertEquals(new LocationId(20), shipment.destinationCityId());
        assertEquals("SUCURSAL DESTINATION", shipment.exactAddress());
        assertEquals(new BranchID(1), shipment.originBranchId());
        assertEquals(new BranchID(2), shipment.destinationBranchId());
    }

    @Test
    void createBranchToBranchShipment_ThrowsExceptionWhenBranchNotFound() {
        UserId senderId = UserId.generate();
        Recipient recipient = Recipient.create("Jane", null, "Doe", null, new Phone("88888888"), new Email("jane@example.com"));
        List<Package> packages = List.of(Package.create("Books", Weight.ofKg(2.0), Dimensions.ofCm(10, 10, 10)));

        when(branchRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BranchNotFoundException.class, () ->
            shipmentDomainService.createBranchToBranchShipment(senderId, recipient, 1, 2, packages, "SBX")
        );
    }

    @Test
    void createBranchToBranchShipment_ThrowsExceptionWhenBranchInactive() {
        UserId senderId = UserId.generate();
        Recipient recipient = Recipient.create("Jane", null, "Doe", null, new Phone("88888888"), new Email("jane@example.com"));
        List<Package> packages = List.of(Package.create("Books", Weight.ofKg(2.0), Dimensions.ofCm(10, 10, 10)));

        Branch originBranch = Branch.builder()
                .id(new BranchID(1))
                .name("ORIGIN")
                .city(new LocationId(10))
                .phone(new Phone("88888888"))
                .deletedAt(LocalDateTime.now())
                .build();

        when(branchRepository.findById(1)).thenReturn(Optional.of(originBranch));

        assertThrows(BranchInactiveException.class, () ->
            shipmentDomainService.createBranchToBranchShipment(senderId, recipient, 1, 2, packages, "SBX")
        );
    }
}
