package com.cursogabriel.libraryapi.service;

import com.cursogabriel.libraryapi.model.entity.Book;

import java.util.Optional;

public interface BookService {
    Book save(Book any);

    Optional<Book> getById(Long id);

}
