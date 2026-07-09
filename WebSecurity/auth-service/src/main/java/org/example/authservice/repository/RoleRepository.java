package org.example.authservice.repository;

import org.example.authservice.entity.Role;
import org.example.authservice.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find a role by its name.
     */
    Optional<Role> findByRoleName(RoleType roleName);

    /**
     * Check if a role exists.
     */
    boolean existsByRoleName(RoleType roleName);

}
