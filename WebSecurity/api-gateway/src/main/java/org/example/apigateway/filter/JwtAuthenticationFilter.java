package org.example.apigateway.filter;

import lombok.RequiredArgsConstructor;
import org.example.security.JwtTokenProvider;
import org.example.security.SecurityConstants;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtTokenProvider jwtTokenProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/register"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (pathMatcher.match("/api/notifications/internal", path)) {
            String token = resolveToken(exchange.getRequest());
            if (!StringUtils.hasText(token)
                    || !jwtTokenProvider.validateToken(token)
                    || !SecurityConstants.TOKEN_TYPE_SERVICE.equals(jwtTokenProvider.extractTokenType(token))) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        }

        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        if (pathMatcher.match("/api/posts", path) || pathMatcher.match("/api/posts/*", path)) {
            if (exchange.getRequest().getMethod().name().equals("GET")) {
                String token = resolveToken(exchange.getRequest());
                if (!StringUtils.hasText(token)) {
                    return chain.filter(exchange);
                }
            }
        }

        String token = resolveToken(exchange.getRequest());
        if (!StringUtils.hasText(token) || !jwtTokenProvider.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String username = jwtTokenProvider.extractUsername(token);
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-Name", username)
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(publicPath -> pathMatcher.match(publicPath, path));
    }

    private String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(SecurityConstants.BEARER_PREFIX)) {
            return bearerToken.substring(SecurityConstants.BEARER_PREFIX.length());
        }
        return null;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
