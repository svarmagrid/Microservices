package org.example.postservice.repository;

import org.example.postservice.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByPostIdAndUsername(Long postId, String username);

    Optional<PostLike> findByPostIdAndUsername(Long postId, String username);
}
