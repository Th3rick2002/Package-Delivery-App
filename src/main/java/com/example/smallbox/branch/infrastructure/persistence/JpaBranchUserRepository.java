package com.example.smallbox.branch.infrastructure.persistence;

import com.example.smallbox.branch.infrastructure.persistence.entities.BranchUserJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaBranchUserRepository extends JpaRepository<BranchUserJpaEntity, BranchUserJpaEntity.BranchUserId> {
    Page<BranchUserJpaEntity> findByBranchId(Integer branchId, Pageable pageable);
    Optional<BranchUserJpaEntity> findByUserId(UUID userId);
}
