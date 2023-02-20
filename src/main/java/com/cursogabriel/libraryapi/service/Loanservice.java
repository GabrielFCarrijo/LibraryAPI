package com.cursogabriel.libraryapi.service;

import com.cursogabriel.libraryapi.dto.LoanFilterDTO;
import com.cursogabriel.libraryapi.model.entity.Loan;
import com.cursogabriel.libraryapi.resource.BookController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface Loanservice {
    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);

    Page<Loan> find(LoanFilterDTO filterDTO, Pageable pageable);
}
