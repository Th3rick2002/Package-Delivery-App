package com.example.smallbox.shipment.infrastructure.persistence.entities;

import com.example.smallbox.shared.infrastructure.persistence.entities.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "recipient")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE recipient SET deleted_at = now() WHERE recipient_id = ?")
@SQLRestriction(value = "deleted_at IS NULL")
public class RecipientJpaEntity extends AuditableEntity {
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
}
