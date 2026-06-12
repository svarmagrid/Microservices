package org.example.bookservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="books")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;
}
