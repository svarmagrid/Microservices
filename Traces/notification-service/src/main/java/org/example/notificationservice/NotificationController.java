package org.example.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    /**
     * POST /notifications/send
     * Called after post creation to send a notification.
     * If invoked via the API Gateway, it will participate in the same trace.
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendNotification(@RequestBody Map<String, String> body) {
        String userId = body.getOrDefault("userId", "unknown");
        String event = body.getOrDefault("event", "unknown");
        log.info("Processing notification: event={} for userId={} [part of distributed trace]", event, userId);
        return ResponseEntity.ok(Map.of("status", "sent", "userId", userId, "event", event));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "notification-service"));
    }
}
