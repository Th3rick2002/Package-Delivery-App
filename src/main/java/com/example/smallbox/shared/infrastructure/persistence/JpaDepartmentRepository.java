package com.example.smallbox.shared.infrastructure.persistence;

import com.example.smallbox.shared.infrastructure.persistence.entities.DepartmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDepartmentRepository extends JpaRepository<DepartmentJpaEntity, Integer> {
}
