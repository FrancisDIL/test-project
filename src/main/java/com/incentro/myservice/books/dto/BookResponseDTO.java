package com.incentro.myservice.books.dto;

import com.incentro.myservice.books.entity.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookResponseDTO {
    private Long id;
    private String title;
    private String author;
    private Book.BookStatus status;
}