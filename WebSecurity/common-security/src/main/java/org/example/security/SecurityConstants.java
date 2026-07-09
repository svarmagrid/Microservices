package org.example.security;

public final class SecurityConstants {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String ROLES_CLAIM = "roles";
    public static final String SERVICE_CLAIM = "service";
    public static final String TOKEN_TYPE_CLAIM = "token_type";
    public static final String TOKEN_TYPE_USER = "USER";
    public static final String TOKEN_TYPE_SERVICE = "SERVICE";

    private SecurityConstants() {
    }
}
