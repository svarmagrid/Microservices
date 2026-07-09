package org.example.postservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

/**
 * Feign client for auth-service.
 * Micrometer Tracing automatically injects B3 trace headers (X-B3-TraceId,
 * X-B3-SpanId, X-B3-ParentSpanId) into every Feign request, enabling
 * Zipkin to correlate all spans from a single user request across services.
 */
@FeignClient(name = "auth-service", url = "${auth.service.url}")
public interface AuthClient {

    @GetMapping("/auth/validate")
    Map<String, Object> validateToken(@RequestHeader("Authorization") String authHeader);
}
