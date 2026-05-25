package com.example.smallbox.user.domain;

import com.example.smallbox.shared.domain.Email;
import com.example.smallbox.shared.domain.Phone;
import com.example.smallbox.shared.domain.UserId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    private final UserId id;
    private final Role role;
    private final String firstName;
    private final String secondName;
    private final String lastName;
    private final String secondLastName;
    private final Phone phone;
    private final Email email;
    private String hashPassword;
    
    private LocalDateTime lastLogin;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static User create(
            Role role,
            String firstName,
            String secondName,
            String lastName,
            String secondLastName,
            Phone phone,
            Email email,
            String hashPassword
    ) {
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("First name is required");
        if (role == null) throw new IllegalArgumentException("Role is required");
        if (email == null) throw new IllegalArgumentException("Email is required");
        if (hashPassword == null || hashPassword.isBlank()) throw new IllegalArgumentException("Password is required");

        return User.builder()
                .id(UserId.generate())
                .role(role)
                .firstName(firstName)
                .secondName(secondName)
                .lastName(lastName)
                .secondLastName(secondLastName)
                .phone(phone)
                .email(email)
                .hashPassword(hashPassword)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void updatePassword(String newHashPassword) {
        if (newHashPassword == null || newHashPassword.isBlank())
            throw new IllegalArgumentException("Password cannot be empty");

        this.hashPassword = newHashPassword;
        this.updatedAt = LocalDateTime.now();
    }

    public void recordLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void deleted() {
        if (this.deletedAt != null) return;
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
