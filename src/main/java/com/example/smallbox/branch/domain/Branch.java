package com.example.smallbox.branch.domain;

import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.LocationId;
import com.example.smallbox.shared.domain.Phone;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class Branch {
    private final BranchID id;
    private String name;
    private final LocationId city;
    private final Phone phone;

    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static Branch create(
            String name,
            LocationId city,
            Phone phone
    ) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");

        name = name.trim().toUpperCase();

        return Branch.builder()
                .id(new BranchID(null))
                .name(name)
                .city(city)
                .phone(phone)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void updateName(String newName) {
        if (newName == null || newName.isBlank())
            throw new IllegalArgumentException("Name cannot be empty");

        newName = newName.trim().toUpperCase();

        this.name = newName;
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        if (this.deletedAt != null) return;
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
