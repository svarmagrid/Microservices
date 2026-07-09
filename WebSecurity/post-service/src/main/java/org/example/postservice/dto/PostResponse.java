package org.example.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private Long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
