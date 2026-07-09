package org.example.notificationservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.notificationservice.dto.CreateNotificationRequest;
import org.example.notificationservice.dto.NotificationResponse;
import org.example.notificationservice.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/internal")
    public ResponseEntity<NotificationResponse> createInternalNotification(
            @Valid @RequestBody CreateNotificationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationService.createInternalNotification(request));
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications() {
        return ResponseEntity.ok(notificationService.getMyNotifications());
    }
}
