package com.incentro.myservice.books.service;

import com.incentro.myservice.application.exception.AppInputErrorException;
import com.incentro.myservice.books.dto.AddBookDTO;
import com.incentro.myservice.books.dto.BookResponseDTO;
import com.incentro.myservice.books.dto.EditBookDTO;
import com.incentro.myservice.books.entity.Book;
import com.incentro.myservice.books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookServiceImpl implements BooksService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public BookResponseDTO addBook(AddBookDTO addBookDTO) {
        Book book = new Book();
        book.setTitle(addBookDTO.getTitle());
        book.setAuthor(addBookDTO.getAuthor());
        Book savedBook = bookRepository.save(book);
        return savedBook.toBookResponse();
    }

    @Override
    @PreAuthorize("hasAuthority('MEMBER')")
    public BookResponseDTO getBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(
                () -> new AppInputErrorException("book.does.not.exist")).toBookResponse();
    }

    @Override
    @PreAuthorize("hasAuthority('MEMBER')")
    public List<BookResponseDTO> getBooks() {
        List<Book> allBooks = bookRepository.findAll();
        List<BookResponseDTO> books = new ArrayList<>();

        for (Book book : allBooks) {
            books.add(book.toBookResponse());
        }
        return books;
    }

    @Override
    @PreAuthorize("hasAuthority('MEMBER')")
    public BookResponseDTO borrowBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new AppInputErrorException("book.does.not.exist"));

        if (book.getStatus() == Book.BookStatus.BORROWED) {
            throw new AppInputErrorException("book.not.available");
        } else {
            book.setStatus(Book.BookStatus.BORROWED);
        }

        return bookRepository.save(book).toBookResponse();
    }

    @Override
    public BookResponseDTO returnBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new AppInputErrorException("book.does.not.exist"));

        if (book.getStatus() != Book.BookStatus.BORROWED) {
            throw new AppInputErrorException("book.not.returnable");
        } else {
            book.setStatus(Book.BookStatus.AVAILABLE);
        }
        return bookRepository.save(book).toBookResponse();
    }

    @Override
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public BookResponseDTO editBook(EditBookDTO editBookDTO) {
        Book book = bookRepository.findById(editBookDTO.getId()).orElseThrow(
                () -> new AppInputErrorException("book.does.not.exist"));

        if (editBookDTO.getAuthor() != null) {
            book.setAuthor(editBookDTO.getAuthor());
        }

        if (editBookDTO.getTitle() != null) {
            book.setTitle(editBookDTO.getTitle());
        }

        if (editBookDTO.getStatus() != null) {
            book.setStatus(editBookDTO.getStatus());
        }

        return bookRepository.save(book).toBookResponse();
    }

    @Override
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new AppInputErrorException("book.does.not.exist"));

        bookRepository.delete(book);
    }
}