package com.example.smallbox.user.infrastructure.persistence;

import com.example.smallbox.shared.domain.Email;
import com.example.smallbox.shared.domain.Phone;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.user.domain.User;

import java.util.UUID;

public class UserMapper {
    
    public static User toDomain(UserJpaEntity entity) {
        if (entity == null) return null;
        
        return User.builder()
                .id(new UserId(entity.getUserId()))
                .role(RoleMapper.toDomain(entity.getRole()))
                .firstName(entity.getFirstName())
                .secondName(entity.getSecondName())
                .lastName(entity.getLastName())
                .secondLastName(entity.getSecondLastName())
                .phone(new Phone(entity.getPhone()))
                .email(new Email(entity.getEmail()))
                .hashPassword(entity.getHashPassword())
                .lastLogin(entity.getLastLogin())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }
    
    public static UserJpaEntity toJpaEntity(User user) {
        if (user == null) return null;
        
        UserJpaEntity entity = new UserJpaEntity();
        entity.setUserId(user.getId().uuid());
        entity.setRole(RoleMapper.toJpaEntity(user.getRole()));
        entity.setFirstName(user.getFirstName());
        entity.setSecondName(user.getSecondName());
        entity.setLastName(user.getLastName());
        entity.setSecondLastName(user.getSecondLastName());
        entity.setPhone(user.getPhone().value());
        entity.setEmail(user.getEmail().value());
        entity.setHashPassword(user.getHashPassword());
        entity.setLastLogin(user.getLastLogin());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        entity.setDeletedAt(user.getDeletedAt());
        return entity;
    }
}
