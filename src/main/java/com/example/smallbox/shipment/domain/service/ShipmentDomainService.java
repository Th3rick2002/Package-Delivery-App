package com.example.smallbox.shipment.domain.service;

import com.example.smallbox.branch.domain.Branch;
import com.example.smallbox.branch.domain.exception.BranchInactiveException;
import com.example.smallbox.branch.domain.exception.BranchNotFoundException;
import com.example.smallbox.branch.domain.port.BranchRepository;
import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.LocationId;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.domain.Package;
import com.example.smallbox.shipment.domain.Recipient;
import com.example.smallbox.shipment.domain.Shipment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipmentDomainService {

    private final BranchRepository branchRepository;

    public Shipment createBranchToBranchShipment(
            UserId senderId,
            Recipient recipient,
            Integer originBranchId,
            Integer destinationBranchId,
            List<Package> packages,
            String trackingPrefix
    ) {
        Branch originBranch = branchRepository.findById(originBranchId)
                .orElseThrow(() -> new BranchNotFoundException(originBranchId));

        if (originBranch.getDeletedAt() != null) {
            throw new BranchInactiveException(originBranchId);
        }

        Branch destinationBranch = branchRepository.findById(destinationBranchId)
                .orElseThrow(() -> new BranchNotFoundException(destinationBranchId));

        if (destinationBranch.getDeletedAt() != null) {
            throw new BranchInactiveException(destinationBranchId);
        }

        LocationId destinationCityId = destinationBranch.getCity();
        String exactAddress = "SUCURSAL " + destinationBranch.getName();

        return Shipment.create(
                senderId,
                recipient,
                destinationCityId,
                exactAddress,
                new BranchID(originBranchId),
                new BranchID(destinationBranchId),
                packages,
                trackingPrefix
        );
    }
}
