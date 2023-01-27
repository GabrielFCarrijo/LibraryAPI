package com.cursogabriel.libraryapi.service.impl;

import com.cursogabriel.libraryapi.exeption.BusinessException;
import com.cursogabriel.libraryapi.model.entity.Book;
import com.cursogabriel.libraryapi.model.repository.BookRepository;
import com.cursogabriel.libraryapi.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if (repository.existsByISbn(book.getIsbn())) {
            throw new BusinessException("ISBN ja cadastrada");
        }
        return repository.save(book);
    }
}
