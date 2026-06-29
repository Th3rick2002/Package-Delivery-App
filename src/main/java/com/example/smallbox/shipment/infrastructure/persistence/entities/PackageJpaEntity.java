package com.example.smallbox.shipment.infrastructure.persistence.entities;

import com.example.smallbox.shared.infrastructure.persistence.entities.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "package")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE package SET deleted_at = now() WHERE package_id = ?")
@SQLRestriction(value = "deleted_at IS NULL")
public class PackageJpaEntity extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private Long packageId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shipment_id", nullable = false)
    private ShipmentJpaEntity shipment;

    @Column(name = "package_type_id")
    private Integer packageTypeId;

    @Column(name = "height", precision = 10, scale = 2)
    private BigDecimal height;

    @Column(name = "width", precision = 10, scale = 2)
    private BigDecimal width;

    @Column(name = "length", precision = 10, scale = 2)
    private BigDecimal length;

    @Column(name = "weight", precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "fragile")
    private Boolean fragile;
}
