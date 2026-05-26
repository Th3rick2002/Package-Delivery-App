package com.example.smallbox.branch.infrastructure.persistence.entities;

import com.example.smallbox.shared.infrastructure.persistence.entities.DepartmentJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "branch")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE branch SET deleted_at = now(), deleted_by = ? WHERE branch_id = ?")
@SQLRestriction(value = "deleted_at IS NULL")
public class BranchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private Integer branchId;

    @Column(name = "name_branch", nullable = false, length = 100)
    private String nameBranch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DepartmentJpaEntity department;

    @Column(name = "phone_branch", nullable = false, length = 20)
    private String phoneBranch;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
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
