package com.example.smallbox.branch.infrastructure.persistence;

import com.example.smallbox.branch.infrastructure.persistence.entities.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaBranchRepository extends JpaRepository<BranchEntity, Integer> {
    Optional<BranchEntity> findByDepartment_IdDepartment(Integer departmentId);
}
