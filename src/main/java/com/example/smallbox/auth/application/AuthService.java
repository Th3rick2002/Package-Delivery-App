package com.example.smallbox.auth.application;

import com.example.smallbox.auth.application.dto.LoginRequest;
import com.example.smallbox.auth.infrastructure.security.jwt.JwtTokenDTO;
import com.example.smallbox.auth.infrastructure.security.service.CustomUserPrincipal;
import com.example.smallbox.auth.infrastructure.security.service.StaffUserPrincipal;
import com.example.smallbox.branch.domain.BranchUser;
import com.example.smallbox.branch.domain.exception.BranchAssignmentRevokedException;
import com.example.smallbox.branch.domain.exception.BranchUserNotAssignedException;
import com.example.smallbox.branch.domain.exception.BranchUserNotFoundException;
import com.example.smallbox.branch.domain.port.BranchUserRepository;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.user.domain.Role;
import com.example.smallbox.user.domain.exceptions.RoleNotFoundException;
import com.example.smallbox.user.domain.exceptions.UserInvalidRoleException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final BranchUserRepository branchUserRepository;

    public JwtTokenDTO login(LoginRequest request, String ip, String ua) {
        Authentication authentication = authenticate(request);

        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        String role = principal.getAuthorities().iterator().next().getAuthority();

        if (!"ROLE_CLIENT".equals(role)) throw new UserInvalidRoleException("invalid role for client");

        String accessToken = tokenService.generateAccessToken(principal);
        String refreshToken = tokenService.generateRefreshToken(principal);

        refreshTokenService.saveSession(principal.getUserId(), refreshToken, ip, ua);

        return new JwtTokenDTO(accessToken, refreshToken);
    }

    public JwtTokenDTO loginAdministrative(LoginRequest request, String ip, String ua) {
        Authentication authentication = authenticate(request);
        CustomUserPrincipal basePrincipal = (CustomUserPrincipal) authentication.getPrincipal();

        String role = basePrincipal.getAuthorities().iterator().next().getAuthority();
        Integer branchId = null;

        if ("ROLE_BRANCH_ADMIN".equals(role) || "ROLE_EMPLOYEE".equals(role)) {
            UserId userId = new UserId(basePrincipal.getUserId());
            BranchUser assigned = branchUserRepository.findByUserId(userId)
                    .orElseThrow(() -> new BranchUserNotAssignedException(basePrincipal.getUserId()));

            if (!assigned.isActive())
                throw new BranchAssignmentRevokedException(userId.toString());

            branchId = assigned.getBranchId().id();
        } else if (!"ROLE_SUPER_ADMIN".equals(role)) {
            throw new UserInvalidRoleException("invalid role for internal staff");
        }

        StaffUserPrincipal principal = new StaffUserPrincipal(
                basePrincipal.getUserId(),
                basePrincipal.getUsername(),
                null,
                basePrincipal.getAuthorities(),
                branchId
        );

        String accessToken = tokenService.generateInternalAccessToken(principal);
        String refreshToken = tokenService.generateRefreshToken(basePrincipal);

        refreshTokenService.saveSession(principal.getUserId(), refreshToken, ip, ua);

        return new JwtTokenDTO(accessToken, refreshToken);


    }

    private Authentication authenticate(LoginRequest request) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
    }
}
