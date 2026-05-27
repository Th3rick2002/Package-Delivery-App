package com.example.smallbox.branch.infrastructure.persistence.mapper;

import com.example.smallbox.branch.domain.BranchUser;
import com.example.smallbox.branch.infrastructure.persistence.entities.BranchUserJpaEntity;
import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.UserId;

public class BranchUserMapper {

    public static BranchUser toDomain(BranchUserJpaEntity entity) {
        if (entity == null) return null;

        return BranchUser.builder()
                .branchId(new BranchID(entity.getBranchId()))
                .userId(new UserId(entity.getUserId()))
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    public static BranchUserJpaEntity toJpaEntity(BranchUser domain) {
        if (domain == null) return null;

        BranchUserJpaEntity entity = new BranchUserJpaEntity();
        entity.setBranchId(domain.getBranchId().id());
        entity.setUserId(domain.getUserId().uuid());
        entity.setActive(domain.isActive());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setDeletedAt(domain.getDeletedAt());
        return entity;
    }
}
