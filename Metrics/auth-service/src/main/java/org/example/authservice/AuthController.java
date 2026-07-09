package org.example.authservice;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final Counter loginSuccessCounter;
    private final Counter loginFailureCounter;

    public AuthController(MeterRegistry meterRegistry) {
        // Custom application-specific metrics
        this.loginSuccessCounter = Counter.builder("auth.login.success")
                .description("Number of successful login attempts")
                .register(meterRegistry);
        this.loginFailureCounter = Counter.builder("auth.login.failure")
                .description("Number of failed login attempts")
                .register(meterRegistry);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        String username = request.getOrDefault("username", "unknown");
        String password = request.getOrDefault("password", "");

        // Simulate simple validation
        if (username.isBlank() || password.isBlank()) {
            loginFailureCounter.increment();
            log.warn("Login failed for user: {} - missing credentials", username);
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        loginSuccessCounter.increment();
        log.info("Login successful for user: {}", username);
        return ResponseEntity.ok(Map.of("token", "eyJhbGciOiJIUzI1NiJ9.mock.token", "username", username));
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("valid", false));
        }
        return ResponseEntity.ok(Map.of("valid", true));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "auth-service"));
    }
}
