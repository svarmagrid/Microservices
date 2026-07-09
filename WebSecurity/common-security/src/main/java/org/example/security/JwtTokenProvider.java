package org.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private SecretKey signingKey;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateUserToken(String username, Set<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.ROLES_CLAIM, roles);
        claims.put(SecurityConstants.TOKEN_TYPE_CLAIM, SecurityConstants.TOKEN_TYPE_USER);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(signingKey)
                .compact();
    }

    public String generateServiceToken(String serviceName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.ROLES_CLAIM, Set.of(RoleType.ROLE_SERVICE.name()));
        claims.put(SecurityConstants.SERVICE_CLAIM, serviceName);
        claims.put(SecurityConstants.TOKEN_TYPE_CLAIM, SecurityConstants.TOKEN_TYPE_SERVICE);

        return Jwts.builder()
                .claims(claims)
                .subject(serviceName)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getServiceExpiration()))
                .signWith(signingKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractServiceName(String token) {
        Object service = extractAllClaims(token).get(SecurityConstants.SERVICE_CLAIM);
        return service != null ? service.toString() : null;
    }

    public String extractTokenType(String token) {
        Object tokenType = extractAllClaims(token).get(SecurityConstants.TOKEN_TYPE_CLAIM);
        return tokenType != null ? tokenType.toString() : SecurityConstants.TOKEN_TYPE_USER;
    }

    @SuppressWarnings("unchecked")
    public Set<String> extractRoles(String token) {
        Object rolesObject = extractAllClaims(token).get(SecurityConstants.ROLES_CLAIM);
        if (rolesObject instanceof Collection<?> collection) {
            return collection.stream()
                    .map(Object::toString)
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    public Long getExpirationTime() {
        return jwtProperties.getExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
