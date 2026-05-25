package com.example.smallbox.shared.domain;

import java.util.UUID;

public record UserId(UUID uuid) {
    public  UserId {
        if (uuid == null)
            throw new IllegalArgumentException("User id cannot be null");
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId fromString(String uuid) {
        return new UserId(UUID.fromString(uuid));
    }
}
