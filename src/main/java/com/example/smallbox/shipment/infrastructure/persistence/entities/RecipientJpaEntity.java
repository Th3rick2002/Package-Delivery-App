package com.example.smallbox.shipment.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "recipient")
@Getter
@Setter
@NoArgsConstructor
public class RecipientJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "recipient_id")
    private UUID recipientId;

    @Column(name = "firstname", nullable = false, columnDefinition = "text")
    private String firstName;

    @Column(name = "secondname", columnDefinition = "text")
    private String secondName;

    @Column(name = "lastname", nullable = false, columnDefinition = "text")
    private String lastName;

    @Column(name = "secondlastname", columnDefinition = "text")
    private String secondLastName;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "email", nullable = false, columnDefinition = "text")
    private String email;

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
}
