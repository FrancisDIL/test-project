package com.incentro.myservice.books.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class AddBookDTO {
    @NotBlank(message = "{book.title.empty}")
    @Size(max = 200, message = "{book.title.maximum.size}")
    private String title;

    @NotBlank(message = "{book.author.empty}")
    @Size(max = 200, message = "{book.author.maximum.size}")
    private String author;
}