package com.example.smallbox.user.infrastructure.persistence;

import com.example.smallbox.user.domain.Role;

public class RoleMapper {
    public static Role toDomain(RoleJpaEntity entity) {
        if (entity == null) return null;
        return new Role(entity.getRoleId(), entity.getRoleName());
    }
    
    public static RoleJpaEntity toJpaEntity(Role domain) {
        if (domain == null) return null;
        return new RoleJpaEntity(domain.getId(), domain.getName());
    }
}
