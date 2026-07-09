package org.example.postservice.client;

import feign.RequestInterceptor;
import org.example.postservice.security.ServiceTokenProvider;
import org.example.security.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

public class NotificationFeignConfig {

    @Bean
    public RequestInterceptor serviceTokenInterceptor(ServiceTokenProvider serviceTokenProvider) {
        return template -> template.header(
                HttpHeaders.AUTHORIZATION,
                SecurityConstants.BEARER_PREFIX + serviceTokenProvider.getServiceToken()
        );
    }
}
