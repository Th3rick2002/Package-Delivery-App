package com.example.smallbox.user.infrastructure.persistence;

import com.example.smallbox.shared.domain.Email;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.user.domain.User;
import com.example.smallbox.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepository {
    private final SpringDataUserRepository userRepository;

    @Override
    public User save(User user) {
        UserJpaEntity entity = UserMapper.toJpaEntity(user);
        return UserMapper.toDomain(userRepository.save(entity));
    }

    @Override
    public Optional<User> findById(UserId id) {
        return userRepository.findById(id.uuid()).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return userRepository.findByEmail(email.value()).map(UserMapper::toDomain);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return userRepository.existsByEmail(email.value());
    }

    @Override
    public void delete(UserId id) {
        userRepository.deleteById(id.uuid());
    }
}
