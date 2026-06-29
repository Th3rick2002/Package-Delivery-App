package com.example.smallbox.user.domain;

import com.example.smallbox.shared.domain.Email;
import com.example.smallbox.shared.domain.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UserId id);
    Optional<User> findByEmail(Email email);
    Page<User> findAll(Pageable pageable);
    boolean existsByEmail(Email email);
    void delete(UserId id, java.util.UUID deletedBy);
}
