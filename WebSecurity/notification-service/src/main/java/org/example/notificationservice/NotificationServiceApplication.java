package org.example.notificationservice;

import org.example.notificationservice.config.AllowedServicesProperties;
import org.example.security.CommonSecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CommonSecurityAutoConfiguration.class)
@EnableConfigurationProperties(AllowedServicesProperties.class)
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
