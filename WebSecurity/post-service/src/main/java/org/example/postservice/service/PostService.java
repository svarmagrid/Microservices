package org.example.postservice.service;

import lombok.RequiredArgsConstructor;
import org.example.postservice.client.NotificationClient;
import org.example.postservice.dto.CreateNotificationRequest;
import org.example.postservice.dto.PostRequest;
import org.example.postservice.dto.PostResponse;
import org.example.postservice.entity.Post;
import org.example.postservice.entity.PostLike;
import org.example.postservice.repository.PostLikeRepository;
import org.example.postservice.repository.PostRepository;
import org.example.postservice.security.PostAuthorizationService;
import org.example.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostAuthorizationService postAuthorizationService;
    private final NotificationClient notificationClient;

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        return toResponse(post);
    }

    @Transactional
    public PostResponse createPost(PostRequest request) {
        String username = SecurityUtils.getCurrentUsername();

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .authorUsername(username)
                .build();

        return toResponse(postRepository.save(post));
    }

    @Transactional
    public PostResponse updatePost(Long id, PostRequest request) {
        postAuthorizationService.assertCanModify(id);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setUpdatedAt(LocalDateTime.now());

        return toResponse(postRepository.save(post));
    }

    @Transactional
    public void deletePost(Long id) {
        postAuthorizationService.assertCanModify(id);
        postRepository.deleteById(id);
    }

    @Transactional
    public PostResponse likePost(Long postId) {
        String username = SecurityUtils.getCurrentUsername();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (postLikeRepository.existsByPostIdAndUsername(postId, username)) {
            throw new IllegalArgumentException("You already liked this post");
        }

        postLikeRepository.save(PostLike.builder()
                .postId(postId)
                .username(username)
                .build());

        post.setLikeCount(post.getLikeCount() + 1);
        Post savedPost = postRepository.save(post);

        if (!post.getAuthorUsername().equals(username)) {
            notificationClient.createInternalNotification(CreateNotificationRequest.builder()
                    .recipientUsername(post.getAuthorUsername())
                    .message(username + " liked your post: " + post.getTitle())
                    .sourceService("post-service")
                    .eventType("POST_LIKED")
                    .build());
        }

        return toResponse(savedPost);
    }

    private PostResponse toResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorUsername(post.getAuthorUsername())
                .likeCount(post.getLikeCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
