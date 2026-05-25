package com.example.smallbox.auth.infrastructure.security.service;

import com.example.smallbox.user.application.UserService;
import com.example.smallbox.user.application.dto.UserAuthData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            UserAuthData user = userService.getUserAuthByEmail(email);
            return new CustomUserPrincipal(
                    user.id(),
                    user.email(),
                    user.hashPassword(),
                    List.of(new SimpleGrantedAuthority(user.role()))
            );
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found: " + email);
        }
    }
}
