package com.incentro.myservice.books.dto;

import com.incentro.myservice.books.entity.Book;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class EditBookDTO {
    @NotNull(message = "{id.empty}")
    private Long id;

    @Size(max = 200, message = "{book.title.maximum.size}")
    private String title;

    @Size(max = 200, message = "{book.author.maximum.size}")
    private String author;

    private Book.BookStatus status;
}
