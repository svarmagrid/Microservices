package org.example.authservice.config;

import org.example.authservice.entity.Role;
import org.example.authservice.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(Role::getRoleName)
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return Boolean.TRUE.equals(user.getAccountNonExpired());
    }

    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE.equals(user.getAccountNonLocked());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return Boolean.TRUE.equals(user.getCredentialsNonExpired());
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getEnabled());
    }
}
