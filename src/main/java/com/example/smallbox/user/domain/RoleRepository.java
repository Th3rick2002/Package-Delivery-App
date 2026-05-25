package com.example.smallbox.user.domain;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByName(String name);
    Optional<Role> findById(Integer id);
}
