package org.example.postservice.client;

import org.example.postservice.dto.CreateNotificationRequest;
import org.example.postservice.dto.NotificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "notification-service",
        url = "${notification-service.url}",
        configuration = NotificationFeignConfig.class
)
public interface NotificationClient {

    @PostMapping("/api/notifications/internal")
    NotificationResponse createInternalNotification(@RequestBody CreateNotificationRequest request);
}
