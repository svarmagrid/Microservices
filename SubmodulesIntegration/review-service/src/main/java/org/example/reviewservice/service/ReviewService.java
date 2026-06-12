package org.example.reviewservice.service;

import lombok.RequiredArgsConstructor;
import org.example.reviewservice.client.BookClient;
import org.example.reviewservice.dto.BookDto;
import org.example.reviewservice.dto.ReviewResponse;
import org.example.reviewservice.entity.Review;
import org.example.reviewservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    @Autowired
    private ReviewRepository repository;
    @Autowired
    private BookClient bookClient;

    public List<Review> getAll() {
        return repository.findAll();
    }

    public Review create(Review review){

        bookClient.getBook(review.getBookId());

        return repository.save(review);
    }

    public ReviewResponse getReview(Long id){

        Review review =
                repository.findById(id)
                        .orElseThrow();

        BookDto book =
                bookClient.getBook(review.getBookId());

        return new ReviewResponse(
                review.getId(),
                review.getComment(),
                book.getTitle()
        );
    }

    public void deleteBook(Long id) {
        bookClient.deleteBook(id);
    }
}
