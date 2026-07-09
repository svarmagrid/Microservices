package org.example.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO used for user registration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 100)
    private String fullName;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100,
            message = "Password must be between 8 and 100 characters")
    private String password;

}
