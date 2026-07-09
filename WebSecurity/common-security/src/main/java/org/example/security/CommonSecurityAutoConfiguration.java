package org.example.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = CommonSecurityAutoConfiguration.class)
@EnableConfigurationProperties(JwtProperties.class)
public class CommonSecurityAutoConfiguration {
}
