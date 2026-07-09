package org.example.authservice.repository;

import org.example.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by username.
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Find a user by either username or email.
     * This allows login using either credential.
     */
    Optional<User> findByUsernameOrEmail(String username, String email);

    /**
     * Check whether a username already exists.
     */
    boolean existsByUsername(String username);

    /**
     * Check whether an email already exists.
     */
    boolean existsByEmail(String email);

}
