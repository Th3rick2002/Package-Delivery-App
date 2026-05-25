package com.example.smallbox.user.application;

import com.example.smallbox.shared.domain.Email;
import com.example.smallbox.shared.domain.Phone;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.user.application.dto.CreateUserRequest;
import com.example.smallbox.user.application.dto.UserAuthData;
import com.example.smallbox.user.application.dto.UserResponse;
import com.example.smallbox.user.domain.Role;
import com.example.smallbox.user.domain.RoleRepository;
import com.example.smallbox.user.domain.User;
import com.example.smallbox.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        Email email = new Email(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User with email " + request.email() + " already exists");
        }

        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        User user = User.create(
                role,
                request.firstName(),
                request.secondName(),
                request.lastName(),
                request.secondLastName(),
                new Phone(request.phone()),
                email,
                passwordEncoder.encode(request.password())
        );

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        return userRepository.findById(new UserId(id))
                .map(this::mapToResponse)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> listUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public void deleteUser(UUID id) {
        userRepository.delete(new UserId(id));
    }

    @Transactional(readOnly = true)
    public UserAuthData getUserAuthByEmail(String email) {
        return userRepository.findByEmail(new Email(email))
                .map(user -> new UserAuthData(
                        user.getId().uuid(),
                        user.getEmail().value(),
                        user.getHashPassword(),
                        user.getRole().getName()
                ))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private UserResponse mapToResponse(User user) {
        String fullName = user.getFirstName() + 
                (user.getSecondName() != null ? " " + user.getSecondName() : "") +
                " " + user.getLastName() +
                (user.getSecondLastName() != null ? " " + user.getSecondLastName() : "");
        
        return new UserResponse(
                user.getId().uuid(),
                fullName,
                user.getEmail().value(),
                user.getPhone().value(),
                user.getRole().getName()
        );
    }
}
