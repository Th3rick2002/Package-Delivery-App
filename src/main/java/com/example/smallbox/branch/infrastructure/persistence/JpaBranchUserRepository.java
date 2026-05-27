package com.example.smallbox.branch.infrastructure.persistence;

import com.example.smallbox.branch.infrastructure.persistence.entities.BranchUserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaBranchUserRepository extends JpaRepository<BranchUserJpaEntity, BranchUserJpaEntity.BranchUserId> {
    List<BranchUserJpaEntity> findByBranchId(Integer branchId);
}
