package com.example.smallbox.shipment.infrastructure.persistence.entities;

import com.example.smallbox.branch.infrastructure.persistence.entities.BranchEntity;
import com.example.smallbox.shared.infrastructure.persistence.entities.DepartmentJpaEntity;
import com.example.smallbox.user.infrastructure.persistence.UserJpaEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shipment")
@SQLRestriction(value = "deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
public class ShipmentJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Long shipmentId;

    @Column(name = "tracking_number", unique = true, length = 30)
    private String trackingNumber;

    @Column(name = "user_id")
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private RecipientJpaEntity recipient;

    @Column(name = "city_id", nullable = false)
    private Integer cityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", insertable = false, updatable = false)
    private DepartmentJpaEntity city;

    @Column(name = "exact_address", nullable = false, columnDefinition = "text")
    private String exactAddress;

    @Column(name = "shipment_status_id")
    private Integer shipmentStatusId;

    @Column(name = "shipment_date")
    private LocalDateTime shipmentDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "branch_from")
    private Integer branchFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_from", insertable = false, updatable = false)
    private BranchEntity originBranch;

    @Column(name = "branch_to")
    private Integer branchTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_to", insertable = false, updatable = false)
    private BranchEntity destinationBranch;

    @Column(name = "processed_by")
    private UUID processedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private UUID deletedBy;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageJpaEntity> packages = new ArrayList<>();

    public void addPackage(PackageJpaEntity packageEntity) {
        packages.add(packageEntity);
        packageEntity.setShipment(this);
    }
}
