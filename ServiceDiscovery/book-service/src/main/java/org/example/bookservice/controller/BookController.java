package org.example.bookservice.controller;


import lombok.RequiredArgsConstructor;
import org.example.bookservice.entity.Book;
import org.example.bookservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public Book save(@RequestBody Book book){
        return bookService.save(book);
    }

    @GetMapping("/{id}")
    public Book getById(@PathVariable Long id){
        return bookService.getById(id);
    }

    @GetMapping
    public List<Book> getAllBooks(){
        return bookService.getAllBooks();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        bookService.delete(id);
    }

    @GetMapping("/debug")
    public Book debug() {
        Book b = new Book();
        b.setTitle("test");
        b.setAuthor("author");
        return b;
    }
}
