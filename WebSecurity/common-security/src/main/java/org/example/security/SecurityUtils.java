package org.example.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return authentication.getName();
    }

    public static Set<String> getCurrentRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Set.of();
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    public static boolean hasRole(String role) {
        return getCurrentRoles().contains(role);
    }

    public static boolean isAdmin() {
        return hasRole(RoleType.ROLE_ADMIN.name());
    }

    public static boolean isServiceToken() {
        return hasRole(RoleType.ROLE_SERVICE.name());
    }
}
