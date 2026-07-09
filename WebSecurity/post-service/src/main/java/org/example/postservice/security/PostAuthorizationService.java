package org.example.postservice.security;

import lombok.RequiredArgsConstructor;
import org.example.postservice.entity.Post;
import org.example.postservice.repository.PostRepository;
import org.example.security.RoleType;
import org.example.security.SecurityUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostAuthorizationService {

    private final PostRepository postRepository;

    public void assertCanModify(Long postId) {
        if (SecurityUtils.hasRole(RoleType.ROLE_ADMIN.name())) {
            return;
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        String currentUser = SecurityUtils.getCurrentUsername();
        if (!post.getAuthorUsername().equals(currentUser)) {
            throw new AccessDeniedException("You can only modify your own posts");
        }
    }
}
