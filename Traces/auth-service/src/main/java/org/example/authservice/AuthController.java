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
     * Called by post-service via Feign to validate tokens.
     * The trace ID is automatically propagated via B3 headers,
     * so this span is linked to the original post-service span in Zipkin.
     */
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        log.info("Token validation request received (trace propagated from caller)");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Token validation failed: missing or malformed Authorization header");
            return ResponseEntity.status(401).body(Map.of("valid", false, "reason", "Missing token"));
        }

        log.debug("Token accepted: {}", authHeader.substring(7, Math.min(27, authHeader.length())));
        return ResponseEntity.ok(Map.of("valid", true, "userId", "user-123"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        log.info("Login for user: {}", body.getOrDefault("username", "unknown"));
        return ResponseEntity.ok(Map.of("token", "eyJhbGciOiJIUzI1NiJ9.mock.token"));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "auth-service"));
    }
}
