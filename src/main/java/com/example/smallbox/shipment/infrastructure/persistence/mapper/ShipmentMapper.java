package com.example.smallbox.shipment.infrastructure.persistence.mapper;

import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.Email;
import com.example.smallbox.shared.domain.LocationId;
import com.example.smallbox.shared.domain.Phone;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.domain.Package;
import com.example.smallbox.shipment.domain.Recipient;
import com.example.smallbox.shipment.domain.Shipment;
import com.example.smallbox.shipment.domain.enums.ShipmentStatus;
import com.example.smallbox.shipment.domain.vo.Dimensions;
import com.example.smallbox.shipment.domain.vo.Price;
import com.example.smallbox.shipment.domain.vo.TrackingNumber;
import com.example.smallbox.shipment.domain.vo.Weight;
import com.example.smallbox.shipment.infrastructure.persistence.entities.PackageJpaEntity;
import com.example.smallbox.shipment.infrastructure.persistence.entities.RecipientJpaEntity;
import com.example.smallbox.shipment.infrastructure.persistence.entities.ShipmentJpaEntity;

import java.time.LocalDateTime;

public class ShipmentMapper {
    private static final int DEFAULT_PACKAGE_TYPE_ID = 1;

    private ShipmentMapper() {
    }

    public static ShipmentJpaEntity toJpaEntity(Shipment shipment) {
        if (shipment == null) return null;

        ShipmentJpaEntity entity = new ShipmentJpaEntity();
        entity.setShipmentId(shipment.shipmentId());
        entity.setTrackingNumber(shipment.trackingNumber().value());
        entity.setUserId(shipment.senderId().uuid());
        entity.setRecipient(toJpaEntity(shipment.recipient()));
        entity.setCityId(shipment.destinationCityId().cityId());
        entity.setExactAddress(shipment.exactAddress());
        entity.setShipmentStatusId(shipment.status().databaseId());
        entity.setShipmentDate(shipment.createdAt());
        entity.setPrice(shipment.totalPrice().amount());
        entity.setBranchFrom(shipment.originBranchId().id());
        entity.setBranchTo(shipment.destinationBranchId().id());
        entity.setCreatedAt(shipment.createdAt());
        entity.setCreatedBy(shipment.senderId().uuid());
        entity.setUpdatedAt(shipment.createdAt());
        entity.setUpdatedBy(shipment.senderId().uuid());

        shipment.packages().stream()
                .map(packageDomain -> toJpaEntity(packageDomain, shipment.createdAt(), shipment.senderId()))
                .forEach(entity::addPackage);

        return entity;
    }

    public static Shipment toDomain(ShipmentJpaEntity entity) {
        if (entity == null) return null;

        return Shipment.rehydrate(
                entity.getShipmentId(),
                TrackingNumber.fromPersisted(entity.getTrackingNumber()),
                new UserId(entity.getUserId()),
                toDomain(entity.getRecipient()),
                new LocationId(entity.getCityId()),
                entity.getExactAddress(),
                new BranchID(entity.getBranchFrom()),
                new BranchID(entity.getBranchTo()),
                ShipmentStatus.fromDatabaseId(entity.getShipmentStatusId()),
                entity.getPackages().stream().map(ShipmentMapper::toDomain).toList(),
                Price.of(entity.getPrice()),
                entity.getCreatedAt()
        );
    }

    private static RecipientJpaEntity toJpaEntity(Recipient recipient) {
        RecipientJpaEntity entity = new RecipientJpaEntity();
        entity.setRecipientId(recipient.recipientId());
        entity.setFirstName(recipient.firstName());
        entity.setSecondName(recipient.secondName());
        entity.setLastName(recipient.lastName());
        entity.setSecondLastName(recipient.secondLastName());
        entity.setPhone(recipient.phone().value());
        entity.setEmail(recipient.email().value());
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    private static Recipient toDomain(RecipientJpaEntity entity) {
        return Recipient.withId(
                entity.getRecipientId(),
                entity.getFirstName(),
                entity.getSecondName(),
                entity.getLastName(),
                entity.getSecondLastName(),
                new Phone(entity.getPhone()),
                new Email(entity.getEmail())
        );
    }

    private static PackageJpaEntity toJpaEntity(Package packageDomain, LocalDateTime createdAt, UserId createdBy) {
        PackageJpaEntity entity = new PackageJpaEntity();
        entity.setPackageId(packageDomain.packageId());
        entity.setPackageTypeId(DEFAULT_PACKAGE_TYPE_ID);
        entity.setHeight(packageDomain.dimensions().height());
        entity.setWidth(packageDomain.dimensions().width());
        entity.setLength(packageDomain.dimensions().length());
        entity.setWeight(packageDomain.weight().value());
        entity.setDescription(packageDomain.description());
        entity.setFragile(false);
        entity.setCreatedAt(createdAt);
        entity.setCreatedBy(createdBy.uuid());
        entity.setUpdatedAt(createdAt);
        entity.setUpdatedBy(createdBy.uuid());
        return entity;
    }

    private static Package toDomain(PackageJpaEntity entity) {
        return new Package(
                entity.getPackageId(),
                entity.getDescription(),
                new Weight(entity.getWeight(), "KG"),
                new Dimensions(entity.getLength(), entity.getWidth(), entity.getHeight(), "CM")
        );
    }
}
