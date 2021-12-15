package com.incentro.myservice.books.web;

import com.incentro.myservice.application.model.AppResponse;
import com.incentro.myservice.books.dto.AddBookDTO;
import com.incentro.myservice.books.dto.BookResponseDTO;
import com.incentro.myservice.books.dto.EditBookDTO;
import com.incentro.myservice.books.service.BooksService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("books")
@Tag(name = "books")
public class BooksController {
    private final BooksService booksService;

    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse> addBook(@RequestBody @Valid AddBookDTO addBookDTO) {
        BookResponseDTO book = booksService.addBook(addBookDTO);
        AppResponse apiOkResponse = new AppResponse(HttpStatus.OK, book);
        return new ResponseEntity<>(apiOkResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(value = "/{bookId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse> getBook(@PathVariable(value = "bookId") Long bookId) {
        BookResponseDTO book = booksService.getBookById(bookId);
        AppResponse apiOkResponse = new AppResponse(HttpStatus.OK, book);
        return new ResponseEntity<>(apiOkResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse> getAllBooks() {
        List<BookResponseDTO> books = booksService.getBooks();
        AppResponse apiOkResponse = new AppResponse(HttpStatus.OK, books);
        return new ResponseEntity<>(apiOkResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse> editBook(@RequestBody @Valid EditBookDTO editBookDTO) {
        BookResponseDTO book = booksService.editBook(editBookDTO);
        AppResponse apiOkResponse = new AppResponse(HttpStatus.OK, book);
        return new ResponseEntity<>(apiOkResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping(value = "/borrow/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse> borrowBook(@PathVariable (value = "bookId") Long bookId) {
        BookResponseDTO book = booksService.borrowBook(bookId);
        AppResponse apiOkResponse = new AppResponse(HttpStatus.OK, book);
        return new ResponseEntity<>(apiOkResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping(value = "/return/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse> returnBook(@PathVariable (value = "bookId") Long bookId) {
        BookResponseDTO book = booksService.returnBook(bookId);
        AppResponse apiOkResponse = new AppResponse(HttpStatus.OK, book);
        return new ResponseEntity<>(apiOkResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{bookId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse> deleteBook(@PathVariable (value = "bookId") Long bookId) {
        booksService.deleteBook(bookId);
        AppResponse apiOkResponse = new AppResponse(HttpStatus.OK, "Book deleted successfully");
        return new ResponseEntity<>(apiOkResponse, new HttpHeaders(), HttpStatus.OK);
    }
}