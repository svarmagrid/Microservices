package org.example.reviewservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="reviews")
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;

    private String comment;
}