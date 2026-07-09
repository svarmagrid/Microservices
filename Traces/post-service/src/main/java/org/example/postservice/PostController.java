package org.example.postservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final AuthClient authClient;
    private final Map<Long, Map<String, Object>> posts = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    /**
     * GET /posts — list all posts (no auth required for reading).
     * Single span: only post-service is involved.
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllPosts() {
        log.info("Fetching all posts [single service - one Zipkin span]");
        return ResponseEntity.ok(new ArrayList<>(posts.values()));
    }

    /**
     * POST /posts — create a post (validates JWT with auth-service first).
     * This creates a MULTI-SERVICE TRACE visible in Zipkin:
     *   1. post-service span starts
     *   2. Feign call to auth-service creates a child span (same traceId, new spanId)
     *   3. post-service span completes
     *
     * In Zipkin UI, you will see both spans grouped under one traceId.
     */
    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Map<String, Object> body) {

        log.info("Creating post - first validating token with auth-service [cross-service trace]");

        // This Feign call propagates the current traceId to auth-service
        // Zipkin will show both spans linked under the same trace
        Map<String, Object> validation;
        try {
            validation = authClient.validateToken(authHeader != null ? authHeader : "Bearer invalid");
        } catch (Exception e) {
            log.warn("Token validation call failed: {}", e.getMessage());
            return ResponseEntity.status(401).body(Map.of("error", "Could not validate token"));
        }

        Boolean isValid = (Boolean) validation.getOrDefault("valid", false);
        if (!isValid) {
            log.warn("Token validation returned invalid - denying post creation");
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }

        Long id = idSequence.getAndIncrement();
        body.put("id", id);
        body.put("createdAt", new Date().toString());
        body.put("authorId", validation.get("userId"));
        posts.put(id, body);

        log.info("Post created with id={} [trace spans: post-service + auth-service]", id);
        return ResponseEntity.status(201).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPost(@PathVariable Long id) {
        Map<String, Object> post = posts.get(id);
        if (post == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(post);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "post-service"));
    }
}
