package org.example.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;
    private Long expiration = 86400000L;
    private Long serviceExpiration = 3600000L;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public Long getServiceExpiration() {
        return serviceExpiration;
    }

    public void setServiceExpiration(Long serviceExpiration) {
        this.serviceExpiration = serviceExpiration;
    }
}
