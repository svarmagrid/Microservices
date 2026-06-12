package org.example.reviewservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.reviewservice.dto.ReviewResponse;
import org.example.reviewservice.entity.Review;
import org.example.reviewservice.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    @Autowired
    private ReviewService service;

    @GetMapping
    public List<Review> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Review create(
            @RequestBody Review review){
        return service.create(review);
    }

    @GetMapping("/{id}")
    public ReviewResponse get(
            @PathVariable Long id){
        return service.getReview(id);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable Long id) {
        service.deleteBook(id);
    }
}
