package com.example.smallbox.shipment.application;

import com.example.smallbox.branch.domain.exception.BranchNotFoundException;
import com.example.smallbox.branch.domain.port.BranchRepository;
import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.Email;
import com.example.smallbox.shared.domain.LocationId;
import com.example.smallbox.shared.domain.Phone;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.application.dto.CreateShipmentRequest;
import com.example.smallbox.shipment.application.dto.ShipmentResponse;
import com.example.smallbox.shipment.domain.Package;
import com.example.smallbox.shipment.domain.Recipient;
import com.example.smallbox.shipment.domain.Shipment;
import com.example.smallbox.shipment.domain.port.ShipmentRepository;
import com.example.smallbox.shipment.domain.vo.Dimensions;
import com.example.smallbox.shipment.domain.vo.Weight;
import com.example.smallbox.user.domain.UserRepository;
import com.example.smallbox.user.domain.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateShipmentUseCase {

    private static final String TRACKING_PREFIX = "SBX";

    private final ShipmentRepository shipmentRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;

    @Transactional
    public ShipmentResponse execute(CreateShipmentRequest request) {
        UserId senderId = new UserId(request.senderId());
        userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException(request.senderId().toString()));

        branchRepository.findById(request.originBranchId())
                .orElseThrow(() -> new BranchNotFoundException(request.originBranchId()));

        branchRepository.findById(request.destinationBranchId())
                .orElseThrow(() -> new BranchNotFoundException(request.destinationBranchId()));

        Recipient recipient = buildRecipient(request.recipient());
        List<Package> packages = buildPackages(request.packages());

        Shipment shipment = Shipment.create(
                senderId,
                recipient,
                new LocationId(request.destinationCityId()),
                request.exactAddress(),
                new BranchID(request.originBranchId()),
                new BranchID(request.destinationBranchId()),
                packages,
                TRACKING_PREFIX
        );

        Shipment saved = shipmentRepository.save(shipment);

        return ShipmentResponse.from(saved);
    }

    private Recipient buildRecipient(CreateShipmentRequest.RecipientRequest req) {
        return Recipient.create(
                req.firstName(),
                req.secondName(),
                req.lastName(),
                req.secondLastName(),
                new Phone(req.phone()),
                new Email(req.email())
        );
    }

    private List<Package> buildPackages(List<CreateShipmentRequest.PackageRequest> packageRequests) {
        return packageRequests.stream()
                .map(req -> Package.create(
                        req.description(),
                        new Weight(BigDecimal.valueOf(req.weight()), req.weightUnit()),
                        new Dimensions(
                                BigDecimal.valueOf(req.length()),
                                BigDecimal.valueOf(req.width()),
                                BigDecimal.valueOf(req.height()),
                                req.dimensionsUnit()
                        )
                ))
                .toList();
    }
}
