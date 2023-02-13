package com.cursogabriel.libraryapi.service.impl;

import com.cursogabriel.libraryapi.exeption.BusinessException;
import com.cursogabriel.libraryapi.model.entity.Loan;
import com.cursogabriel.libraryapi.model.repository.LoanRepository;
import com.cursogabriel.libraryapi.service.Loanservice;
import org.springframework.stereotype.Service;

public class LoanServiceImpl implements Loanservice {

    private LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        if (repository.existsByBookAndNotReturnd(loan.getBook())) {
            throw new BusinessException("Book alredy loaned");
        }
        return repository.save(loan);
    }
}
