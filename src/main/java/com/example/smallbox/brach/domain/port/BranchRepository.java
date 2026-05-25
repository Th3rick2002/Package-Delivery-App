package com.example.smallbox.brach.domain.port;

import com.example.smallbox.brach.domain.Branch;

import java.util.List;
import java.util.Optional;

public interface BranchRepository {
    Branch save(Branch branch);
    Optional<Branch> findById(Integer id);
    Optional<Branch> findByLocationId(Integer locationId);
    List<Branch> findAll();
    boolean existsById(Integer id);
    void update(Branch branch);
    void deleteById(Integer id);
}
