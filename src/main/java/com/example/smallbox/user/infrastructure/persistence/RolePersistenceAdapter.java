package com.example.smallbox.user.infrastructure.persistence;

import com.example.smallbox.user.domain.Role;
import com.example.smallbox.user.domain.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RolePersistenceAdapter implements RoleRepository {
    private final SpringDataRoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByRoleName(name).map(RoleMapper::toDomain);
    }

    @Override
    public Optional<Role> findById(Integer id) {
        return roleRepository.findById(id).map(RoleMapper::toDomain);
    }
}
