package com.example.smallbox.shared.infrastructure.persistence;

import com.example.smallbox.shared.domain.LocationId;
import com.example.smallbox.shared.infrastructure.persistence.entities.DepartmentJpaEntity;

public class DepartmentMapper {

    public static DepartmentJpaEntity toJpaEntity(LocationId departmentId) {
        if (departmentId == null) return null;

        DepartmentJpaEntity entity = new DepartmentJpaEntity();
        entity.setIdDepartment(departmentId.cityId());
        return entity;
    }
}
