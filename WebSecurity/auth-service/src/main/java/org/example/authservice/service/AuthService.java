package org.example.authservice.service;

import lombok.RequiredArgsConstructor;
import org.example.authservice.config.UserPrincipal;
import org.example.authservice.dto.AuthResponse;
import org.example.authservice.dto.LoginRequest;
import org.example.authservice.dto.RegisterRequest;
import org.example.authservice.entity.Role;
import org.example.authservice.entity.RoleType;
import org.example.authservice.entity.User;
import org.example.authservice.repository.RoleRepository;
import org.example.authservice.repository.UserRepository;
import org.example.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role userRole = roleRepository.findByRoleName(RoleType.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("Default role ROLE_USER not found"));

        User user = User.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(userRole))
                .build();

        User savedUser = userRepository.save(user);
        return buildAuthResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsernameOrEmail(
                        request.getUsernameOrEmail(),
                        request.getUsernameOrEmail()
                )
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        request.getPassword()
                )
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return buildAuthResponse(principal.getUser());
    }

    public AuthResponse getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getRoleName().name())
                .collect(Collectors.toSet());

        String token = jwtTokenProvider.generateUserToken(user.getUsername(), roles);

        return AuthResponse.builder()
                .accessToken(token)
                .expiresIn(jwtTokenProvider.getExpirationTime())
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }
}
