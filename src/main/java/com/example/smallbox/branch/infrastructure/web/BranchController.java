package com.example.smallbox.branch.infrastructure.web;

import com.example.smallbox.branch.application.BranchService;
import com.example.smallbox.branch.application.dto.BranchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
