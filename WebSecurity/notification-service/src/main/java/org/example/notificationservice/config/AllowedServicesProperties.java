package org.example.notificationservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "allowed-services")
@Getter
@Setter
public class AllowedServicesProperties {

    private List<String> internalNotificationCreators = List.of("post-service");
}
