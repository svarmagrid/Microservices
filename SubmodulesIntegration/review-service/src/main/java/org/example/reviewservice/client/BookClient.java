package org.example.reviewservice.client;

import org.example.reviewservice.dto.BookDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "book-service",
        url = "${book.service.url}"
)
public interface BookClient {

    @GetMapping("/books/{id}")
    BookDto getBook(@PathVariable Long id);

    @DeleteMapping("/books/{id}")
    void deleteBook(@PathVariable Long id);
}
