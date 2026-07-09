package org.example.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.example.notificationservice.dto.CreateNotificationRequest;
import org.example.notificationservice.dto.NotificationResponse;
import org.example.notificationservice.entity.Notification;
import org.example.notificationservice.repository.NotificationRepository;
import org.example.notificationservice.security.InternalNotificationAuthorizationService;
import org.example.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final InternalNotificationAuthorizationService authorizationService;

    @Transactional
    public NotificationResponse createInternalNotification(CreateNotificationRequest request) {
        authorizationService.assertServiceCanCreateNotifications();

        Notification notification = Notification.builder()
                .recipientUsername(request.getRecipientUsername())
                .message(request.getMessage())
                .sourceService(request.getSourceService())
                .eventType(request.getEventType())
                .build();

        return toResponse(notificationRepository.save(notification));
    }

    public List<NotificationResponse> getMyNotifications() {
        String username = SecurityUtils.getCurrentUsername();
        authorizationService.assertUserCanReadOwnNotifications(username);

        return notificationRepository.findByRecipientUsernameOrderByCreatedAtDesc(username).stream()
                .map(this::toResponse)
                .toList();
    }

    private NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .recipientUsername(notification.getRecipientUsername())
                .message(notification.getMessage())
                .sourceService(notification.getSourceService())
                .eventType(notification.getEventType())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
