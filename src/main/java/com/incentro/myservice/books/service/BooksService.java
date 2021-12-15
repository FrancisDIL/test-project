package com.incentro.myservice.books.service;

import com.incentro.myservice.books.dto.AddBookDTO;
import com.incentro.myservice.books.dto.BookResponseDTO;
import com.incentro.myservice.books.dto.EditBookDTO;

import java.util.List;

public interface BooksService {
    BookResponseDTO addBook(AddBookDTO addBookDTO);
    BookResponseDTO getBookById(Long userId);
    List<BookResponseDTO> getBooks();
    BookResponseDTO borrowBook(Long bookId);
    BookResponseDTO returnBook(Long bookId);
    BookResponseDTO editBook(EditBookDTO editBookDTO);
    void deleteBook(Long bookId);
}
