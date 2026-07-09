package org.example.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendNotification(@RequestBody Map<String, String> body) {
        String userId = body.getOrDefault("userId", "unknown");
        String message = body.getOrDefault("message", "");
        log.info("Sending notification to userId={}, message='{}'", userId, message);
        // Simulate processing
        log.debug("Notification dispatched successfully for userId={}", userId);
        return ResponseEntity.ok(Map.of("status", "sent", "userId", userId));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        log.debug("Health check called on notification-service");
        return ResponseEntity.ok(Map.of("status", "UP", "service", "notification-service"));
    }
}
