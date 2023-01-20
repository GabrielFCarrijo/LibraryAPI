package com.cursogabriel.libraryapi.model.repository;

import com.cursogabriel.libraryapi.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
