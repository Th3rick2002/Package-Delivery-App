package com.example.smallbox.branch.infrastructure.web;

import com.example.smallbox.branch.application.BranchService;
import com.example.smallbox.branch.application.dto.BranchResponse;
import com.example.smallbox.branch.application.dto.CreateBranchRequest;
import com.example.smallbox.branch.application.dto.UpdateBranchRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/branches")
@Slf4j
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;

    @GetMapping
    public ResponseEntity<List<BranchResponse>> getAll() {
        return ResponseEntity.ok(branchService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(branchService.getById(id));
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<BranchResponse> getByLocationId(@PathVariable Integer locationId) {
        return ResponseEntity.ok(branchService.getByLocationId(locationId));
    }

    @PostMapping
    public ResponseEntity<BranchResponse> create(@Valid @RequestBody CreateBranchRequest request) {
        return ResponseEntity.ok(branchService.create(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BranchResponse> update(@PathVariable Integer id, @Valid @RequestBody UpdateBranchRequest request) {
        return ResponseEntity.ok(branchService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        branchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
