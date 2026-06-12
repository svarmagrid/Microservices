package org.example.reviewservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewResponse {

    private Long reviewId;

    private String comment;

    private String bookTitle;
}
