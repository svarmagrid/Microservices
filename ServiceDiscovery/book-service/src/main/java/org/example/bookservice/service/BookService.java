package org.example.bookservice.service;

import lombok.RequiredArgsConstructor;
import org.example.bookservice.entity.Book;
import org.example.bookservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book save(Book book){
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book getById(Long id){
        return bookRepository.findById(id).orElseThrow();
    }

    public void delete(Long id){
        bookRepository.deleteById(id);
    }

}
