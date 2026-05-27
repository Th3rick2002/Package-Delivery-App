package com.example.smallbox.shipment.domain.port;

import com.example.smallbox.shipment.domain.Package;

import java.util.List;
import java.util.Optional;

public interface PackageRepository {
    Optional<Package> findById(Long id);
    List<Package> findByShipmentId(Long shipmentId);
}
