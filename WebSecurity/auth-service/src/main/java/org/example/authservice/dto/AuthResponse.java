package org.example.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Response DTO returned after successful authentication.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    /**
     * JWT Access Token
     */
    private String accessToken;

    /**
     * Token type (Bearer)
     */
    @Builder.Default
    private String tokenType = "Bearer";

    /**
     * Token expiration time in milliseconds.
     */
    private Long expiresIn;

    /**
     * Logged-in user's ID.
     */
    private Long userId;

    /**
     * Username.
     */
    private String username;

    /**
     * Email address.
     */
    private String email;

    /**
     * Roles assigned to the user.
     */
    private Set<String> roles;

}
