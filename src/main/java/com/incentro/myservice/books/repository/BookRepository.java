package com.incentro.myservice.books.repository;

import com.incentro.myservice.books.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b where b.id = ?1")
    Optional<Book> findById(Long id);
}