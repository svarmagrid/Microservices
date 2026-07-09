package org.example.postservice;

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

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllPosts() {
        log.info("Fetching all posts, count={}", posts.size());
        return ResponseEntity.ok(new ArrayList<>(posts.values()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPost(@PathVariable Long id) {
        log.info("Fetching post with id={}", id);
        Map<String, Object> post = posts.get(id);
        if (post == null) {
            log.warn("Post not found with id={}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPost(@RequestBody Map<String, Object> body) {
        Long id = idSequence.getAndIncrement();
        body.put("id", id);
        body.put("createdAt", new Date().toString());
        posts.put(id, body);
        log.info("Created new post with id={}, title={}", id, body.get("title"));
        return ResponseEntity.status(201).body(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        if (posts.remove(id) == null) {
            log.warn("Attempted to delete non-existent post id={}", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Deleted post with id={}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        log.debug("Health check called on post-service");
        return ResponseEntity.ok(Map.of("status", "UP", "service", "post-service"));
    }
}
