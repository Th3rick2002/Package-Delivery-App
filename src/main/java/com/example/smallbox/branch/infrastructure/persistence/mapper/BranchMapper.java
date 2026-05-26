package com.example.smallbox.branch.infrastructure.persistence.mapper;

import com.example.smallbox.branch.domain.Branch;
import com.example.smallbox.branch.infrastructure.persistence.entities.BranchEntity;
import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.LocationId;
import com.example.smallbox.shared.domain.Phone;
import com.example.smallbox.shared.infrastructure.persistence.DepartmentMapper;

public class BranchMapper {

    public static Branch toDomain(BranchEntity entity) {
        if (entity == null) return null;

        return Branch.builder()
                .id(new BranchID(entity.getBranchId()))
                .name(entity.getNameBranch())
                .city(new LocationId(entity.getDepartment().getIdDepartment()))
                .phone(new Phone(entity.getPhoneBranch()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    public static BranchEntity toJpaEntity(Branch branch) {
        if (branch == null) return null;

        BranchEntity entity = new BranchEntity();
        entity.setBranchId(branch.getId().id());
        entity.setNameBranch(branch.getName());
        entity.setDepartment(DepartmentMapper.toJpaEntity(branch.getCity()));
        entity.setPhoneBranch(branch.getPhone().value());
        entity.setCreatedAt(branch.getCreatedAt());
        entity.setUpdatedAt(branch.getUpdatedAt());
        entity.setDeletedAt(branch.getDeletedAt());
        return entity;
    }
}
