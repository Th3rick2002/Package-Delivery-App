package com.example.smallbox.user.domain;

import com.example.smallbox.shared.domain.Email;
import com.example.smallbox.shared.domain.Phone;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.user.domain.exceptions.UserCreationForbiddenException;
import com.example.smallbox.user.domain.exceptions.UserInvalidRoleException;
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

    private static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    private static final String ROLE_BRANCH_ADMIN = "ROLE_BRANCH_ADMIN";
    private static final String ROLE_CLIENT = "ROLE_CLIENT";

    public static User createEmployee(
            Role creatorRole,
            Role targetRole,
            String firstName,
            String secondName,
            String lastName,
            String secondLastName,
            Phone phone,
            Email email,
            String hashPassword
    ) {
        validateRequiredFields(firstName, lastName, targetRole, email, hashPassword);
        if (creatorRole == null) throw new IllegalArgumentException("Creator role is required");

        if (targetRole.getName().equals(ROLE_CLIENT)) {
            throw new UserInvalidRoleException("Administrators cannot create client accounts through the employee creation process.");
        }

        boolean isAuthorizedCreator = creatorRole.getName().equals(ROLE_SUPER_ADMIN) || creatorRole.getName().equals(ROLE_BRANCH_ADMIN);
        if (!isAuthorizedCreator) {
            throw new UserCreationForbiddenException("Only SUPER_ADMIN or BRANCH_ADMIN can create employees.");
        }

        if (creatorRole.getName().equals(ROLE_BRANCH_ADMIN) && targetRole.getName().equals(ROLE_SUPER_ADMIN)) {
            throw new UserCreationForbiddenException("Branch administrators cannot create super administrators.");
        }

        return User.builder()
                .id(UserId.generate())
                .role(targetRole)
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

    public static User createClient(
            Role role,
            String firstName,
            String secondName,
            String lastName,
            String secondLastName,
            Phone phone,
            Email email,
            String hashPassword
    ) {
        validateRequiredFields(firstName, lastName, role, email, hashPassword);

        if (!role.getName().equals(ROLE_CLIENT)) {
            throw new UserInvalidRoleException("Only CLIENT role can be assigned during self-registration.");
        }

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

    private static void validateRequiredFields(String firstName, String lastName, Role role, Email email, String hashPassword) {
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("First name is required");
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("Last name is required");
        if (role == null) throw new IllegalArgumentException("Role is required");
        if (email == null) throw new IllegalArgumentException("Email is required");
        if (hashPassword == null || hashPassword.isBlank()) throw new IllegalArgumentException("Password is required");
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
        if (this.deletedAt != null) return;
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
