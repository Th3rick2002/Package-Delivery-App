package com.example.smallbox.user.application;

import com.example.smallbox.shared.domain.Email;
import com.example.smallbox.shared.domain.Phone;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shared.application.dto.PaginatedMeta;
import com.example.smallbox.shared.application.dto.PaginatedResponse;
import com.example.smallbox.user.application.dto.CreateClientRequest;
import com.example.smallbox.user.application.dto.CreateUserRequest;
import com.example.smallbox.user.application.dto.UserAuthData;
import com.example.smallbox.user.application.dto.UserResponse;
import com.example.smallbox.user.domain.Role;
import com.example.smallbox.user.domain.RoleRepository;
import com.example.smallbox.user.domain.User;
import com.example.smallbox.user.domain.UserRepository;
import com.example.smallbox.user.domain.exceptions.EmailAlreadyInUseException;
import com.example.smallbox.user.domain.exceptions.RoleNotFoundException;
import com.example.smallbox.user.domain.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Caching(evict = {
            @CacheEvict(value = "users", key = "'all'"),
            @CacheEvict(value = "users", key = "#request.email"),
            @CacheEvict(value = "users_auth", key = "#request.email")
    })
    @Transactional
    public UserResponse createUser(CreateUserRequest request, String creatorEmail) {
        Email email = new Email(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyInUseException(request.email());
        }

        User creator = userRepository.findByEmail(new Email(creatorEmail))
                .orElseThrow(() -> new UserNotFoundException(creatorEmail));

        Role targetRole = roleRepository.findById(request.roleId())
                .orElseThrow(() -> new RoleNotFoundException(request.roleId()));

        User user = User.createEmployee(
                creator.getRole(),
                targetRole,
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

    @Caching(evict = {
            @CacheEvict(value = "users", key = "'all'"),
            @CacheEvict(value = "users", key = "#request.email"),
            @CacheEvict(value = "users_auth", key = "#request.email")
    })
    @Transactional
    public void registerClient(CreateClientRequest request) {
        Email email = new Email(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyInUseException(request.email());
        }

        Role role = roleRepository.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new RoleNotFoundException("ROLE_CLIENT"));

        User user = User.createClient(
                role,
                request.firstName(),
                request.secondName(),
                request.lastName(),
                request.secondLastName(),
                new Phone(request.phone()),
                email,
                passwordEncoder.encode(request.password())
        );

        userRepository.save(user);
    }

    @Cacheable(value = "users", key = "#id")
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        return userRepository.findById(new UserId(id))
                .map(this::mapToResponse)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));
    }

    @Cacheable(value = "users", key = "'all' + #limit + #offset")
    @Transactional(readOnly = true)
    public PaginatedResponse<UserResponse> listUsers(Integer limit, Integer offset) {
        int finalOffset = (offset == null) ? 0 : Math.max(0, offset);
        int finalLimit = (limit == null) ? 20 : limit;
        finalLimit = Math.max(1, Math.min(100, finalLimit));

        int pageNumber = finalOffset / finalLimit;
        Pageable pageable = PageRequest.of(pageNumber, finalLimit);

        Page<User> page = userRepository.findAll(pageable);

        List<UserResponse> data = page.getContent().stream()
                .map(this::mapToResponse)
                .toList();

        PaginatedMeta meta = PaginatedMeta.builder()
                .offset(finalOffset)
                .limit(finalLimit)
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return PaginatedResponse.<UserResponse>builder()
                .data(data)
                .meta(meta)
                .build();
    }

    @Caching(evict = {
            @CacheEvict(value = "users", key = "'all'"),
            @CacheEvict(value = "users", key = "#id"),
            @CacheEvict(value = "users_auth", key = "#id")
    })
    @Transactional
    public void deleteUser(UUID id) {
        userRepository.delete(new UserId(id));
    }

    @Cacheable(value = "users_auth", key = "#email")
    @Transactional(readOnly = true)
    public UserAuthData getUserAuthByEmail(String email) {
        return userRepository.findByEmail(new Email(email))
                .map(user -> new UserAuthData(
                        user.getId().uuid(),
                        user.getEmail().value(),
                        user.getHashPassword(),
                        user.getRole().getName()
                ))
                .orElseThrow(() -> new UserNotFoundException(email));
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
