package org.example.authservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * POST /auth/login
     * Accepts a username and returns a mock JWT token.
     * Demonstrates structured log output to Logstash.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        String username = request.getOrDefault("username", "unknown");
        log.info("Login attempt received for user: {}", username);

        // Mock token response (in a real service this would validate credentials)
        String mockToken = "eyJhbGciOiJIUzI1NiJ9.mock.token";
        log.debug("Generated mock JWT token for user: {}", username);

        return ResponseEntity.ok(Map.of(
                "token", mockToken,
                "username", username
        ));
    }

    /**
     * GET /auth/validate
     * Mock endpoint to validate an Authorization header.
     */
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Token validation failed: missing or malformed Authorization header");
            return ResponseEntity.status(401).body(Map.of("valid", false, "error", "Missing token"));
        }

        log.info("Token validation successful for header: {}", authHeader.substring(0, Math.min(20, authHeader.length())));
        return ResponseEntity.ok(Map.of("valid", true));
    }

    /**
     * GET /auth/health
     * Simple liveness probe.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        log.debug("Health check called on auth-service");
        return ResponseEntity.ok(Map.of("status", "UP", "service", "auth-service"));
    }
}
