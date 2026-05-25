package com.example.smallbox.user.domain;

import com.example.smallbox.shared.domain.Email;
import com.example.smallbox.shared.domain.UserId;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UserId id);
    Optional<User> findByEmail(Email email);
    List<User> findAll();
    boolean existsByEmail(Email email);
    void delete(UserId id);
}
