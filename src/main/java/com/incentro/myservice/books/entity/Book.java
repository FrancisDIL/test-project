package com.incentro.myservice.books.entity;


import com.incentro.myservice.books.dto.BookResponseDTO;

import javax.persistence.*;

@Entity
@Table(name = "books", schema = "public")
public class Book {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "title")
    private String title;
    @Column(name = "author")
    private String author;
    @Column(name = "status")
    private BookStatus status;

    public enum BookStatus {
        AVAILABLE, BORROWED;
    }

    public Book() {
        status = BookStatus.AVAILABLE;
    }

    public Book(Long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.status = BookStatus.AVAILABLE;
    }

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.status = BookStatus.AVAILABLE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", status=" + status +
                '}';
    }

    public BookResponseDTO toBookResponse() {
        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setId(this.id);
        bookResponseDTO.setTitle(this.title);
        bookResponseDTO.setAuthor(this.author);
        bookResponseDTO.setStatus(this.status);

        return bookResponseDTO;
    }
}