package org.example.postservice;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
@RequestMapping("/posts")
public class PostController {

    private final Map<Long, Map<String, Object>> posts = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);
    private final Counter postsCreatedCounter;

    public PostController(MeterRegistry meterRegistry) {
        this.postsCreatedCounter = Counter.builder("posts.created.total")
                .description("Total number of posts created")
                .register(meterRegistry);
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllPosts() {
        return ResponseEntity.ok(new ArrayList<>(posts.values()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPost(@PathVariable Long id) {
        Map<String, Object> post = posts.get(id);
        if (post == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPost(@RequestBody Map<String, Object> body) {
        Long id = idSequence.getAndIncrement();
        body.put("id", id);
        body.put("createdAt", new Date().toString());
        posts.put(id, body);
        postsCreatedCounter.increment();
        return ResponseEntity.status(201).body(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        if (posts.remove(id) == null) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "post-service"));
    }
}
