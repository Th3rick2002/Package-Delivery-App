package com.example.smallbox.shipment.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shipment_history")
@Getter
@NoArgsConstructor
public class ShipmentHistoryJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "shipment_id", nullable = false)
    private Long shipmentId;

    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @Column(name = "changed_by", nullable = false)
    private UUID changedBy;

    @Column(name = "comments")
    private String comments;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ShipmentHistoryJpaEntity(Long shipmentId, Integer statusId, UUID changedBy, String comments, LocalDateTime createdAt) {
        this.shipmentId = shipmentId;
        this.statusId = statusId;
        this.changedBy = changedBy;
        this.comments = comments;
        this.createdAt = createdAt;
    }
}
