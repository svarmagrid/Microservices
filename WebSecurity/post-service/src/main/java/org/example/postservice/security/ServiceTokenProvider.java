package org.example.postservice.security;

import lombok.RequiredArgsConstructor;
import org.example.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceTokenProvider {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${service.name}")
    private String serviceName;

    public String getServiceToken() {
        return jwtTokenProvider.generateServiceToken(serviceName);
    }
}
