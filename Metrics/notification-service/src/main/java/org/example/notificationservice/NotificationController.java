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
        log.info("Sending notification to userId={}", userId);
        return ResponseEntity.ok(Map.of("status", "sent", "userId", userId));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "notification-service"));
    }
}
