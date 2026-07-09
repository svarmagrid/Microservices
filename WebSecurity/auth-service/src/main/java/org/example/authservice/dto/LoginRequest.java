package org.example.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO used for user login.
 * The user can log in using either their username or email.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Username or email is required")
    @Size(max = 150)
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100)
    private String password;

}
