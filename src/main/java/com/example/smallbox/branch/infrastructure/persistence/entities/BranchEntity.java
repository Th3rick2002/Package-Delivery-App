package com.example.smallbox.branch.infrastructure.persistence.entities;

import com.example.smallbox.shared.infrastructure.persistence.entities.AuditableEntity;
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
@SQLDelete(sql = "UPDATE branch SET deleted_at = now() WHERE branch_id = ?")
@SQLRestriction(value = "deleted_at IS NULL")
public class BranchEntity extends AuditableEntity {
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
}
