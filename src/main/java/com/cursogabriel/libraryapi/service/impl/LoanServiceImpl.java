package com.cursogabriel.libraryapi.service.impl;

import com.cursogabriel.libraryapi.dto.LoanFilterDTO;
import com.cursogabriel.libraryapi.exeption.BusinessException;
import com.cursogabriel.libraryapi.model.entity.Loan;
import com.cursogabriel.libraryapi.model.repository.LoanRepository;
import com.cursogabriel.libraryapi.service.Loanservice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public Optional<Loan> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Loan update(Loan loan) {
        return repository.save(loan);
    }

    @Override
    public Page<Loan> find(LoanFilterDTO filterDTO, Pageable pageable) {
        return repository.findByBookIsbnOrCustumer(filterDTO.getIsbn(), filterDTO.getCustomer(), pageable);
    }
}
