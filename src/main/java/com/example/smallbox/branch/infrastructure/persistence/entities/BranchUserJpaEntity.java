package com.example.smallbox.branch.infrastructure.persistence.entities;

import com.example.smallbox.shared.infrastructure.persistence.entities.AuditableEntity;
import com.example.smallbox.user.infrastructure.persistence.UserJpaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "branch_user")
@Getter
@Setter
@NoArgsConstructor
@IdClass(BranchUserJpaEntity.BranchUserId.class)
@SQLDelete(sql = "UPDATE branch_user SET deleted_at = now() WHERE branch_id = ? AND user_id = ?")
@SQLRestriction(value = "deleted_at IS NULL")
public class BranchUserJpaEntity extends AuditableEntity {

    @Id
    @Column(name = "branch_id")
    private Integer branchId;

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", insertable = false, updatable = false)
    private BranchEntity branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserJpaEntity user;

    @Column(name = "active")
    private Boolean active = true;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BranchUserId implements Serializable {
        private Integer branchId;
        private UUID userId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BranchUserId that = (BranchUserId) o;
            return Objects.equals(branchId, that.branchId) && Objects.equals(userId, that.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(branchId, userId);
        }
    }
}
