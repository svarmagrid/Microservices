package org.example.notificationservice.security;

import lombok.RequiredArgsConstructor;
import org.example.notificationservice.config.AllowedServicesProperties;
import org.example.security.RoleType;
import org.example.security.SecurityUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InternalNotificationAuthorizationService {

    private final AllowedServicesProperties allowedServicesProperties;

    public void assertServiceCanCreateNotifications() {
        if (!SecurityUtils.hasRole(RoleType.ROLE_SERVICE.name())) {
            throw new AccessDeniedException("Only trusted services can create notifications");
        }

        String serviceName = SecurityUtils.getCurrentUsername();
        if (!allowedServicesProperties.getInternalNotificationCreators().contains(serviceName)) {
            throw new AccessDeniedException("Service is not allowed to create notifications");
        }
    }

    public void assertUserCanReadOwnNotifications(String recipientUsername) {
        if (SecurityUtils.isServiceToken()) {
            throw new AccessDeniedException("Service tokens cannot read user notifications");
        }

        String currentUser = SecurityUtils.getCurrentUsername();
        if (!currentUser.equals(recipientUsername) && !SecurityUtils.isAdmin()) {
            throw new AccessDeniedException("You can only read your own notifications");
        }
    }
}
