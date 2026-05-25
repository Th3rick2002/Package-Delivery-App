package com.example.smallbox.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Role {
    private final Integer id;
    private final String name;

    public static Role fromName(String name) {
        return new Role(null, name);
    }
}
