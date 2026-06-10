package com.example.smallbox.user.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.smallbox.shared.infrastructure.persistence.entities.AuditableEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = now() WHERE user_id = ?")
@SQLRestriction(value = "deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
public class UserJpaEntity extends AuditableEntity {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleJpaEntity role;

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

    @Column(name = "email", nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "hash_password", nullable = false)
    private String hashPassword;

    @Column(name = "lastlogin")
    private LocalDateTime lastLogin;
}
