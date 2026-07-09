package org.example.authservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.entity.Role;
import org.example.authservice.entity.RoleType;
import org.example.authservice.entity.User;
import org.example.authservice.repository.RoleRepository;
import org.example.authservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initRolesAndAdmin() {
        return args -> {
            Role userRole = roleRepository.findByRoleName(RoleType.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(Role.builder().roleName(RoleType.ROLE_USER).build()));

            Role adminRole = roleRepository.findByRoleName(RoleType.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(Role.builder().roleName(RoleType.ROLE_ADMIN).build()));

            if (!userRepository.existsByUsername("admin")) {
                User admin = User.builder()
                        .fullName("System Admin")
                        .username("admin")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("admin12345"))
                        .roles(Set.of(adminRole, userRole))
                        .build();
                userRepository.save(admin);
                log.info("Default admin user created: admin / admin12345");
            }
        };
    }
}
