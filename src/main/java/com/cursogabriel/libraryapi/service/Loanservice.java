package com.cursogabriel.libraryapi.service;

import com.cursogabriel.libraryapi.model.entity.Loan;
import com.cursogabriel.libraryapi.resource.BookController;

import java.util.Optional;

public interface Loanservice {
    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);
}
