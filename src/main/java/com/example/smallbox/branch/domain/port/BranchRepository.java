package com.example.smallbox.branch.domain.port;

import com.example.smallbox.branch.domain.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

public interface BranchRepository {
    Branch save(Branch branch);
    Optional<Branch> findById(Integer id);
    Optional<Branch> findByLocationId(Integer locationId);
    Page<Branch> findAll(Pageable pageable);
    boolean existsById(Integer id);
    void update(Branch branch);
    void deleteById(Integer id, java.util.UUID deletedBy);
}
