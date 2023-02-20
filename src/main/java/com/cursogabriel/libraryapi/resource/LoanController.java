package com.cursogabriel.libraryapi.resource;

import com.cursogabriel.libraryapi.dto.BookDTO;
import com.cursogabriel.libraryapi.dto.LoanDTO;
import com.cursogabriel.libraryapi.dto.LoanFilterDTO;
import com.cursogabriel.libraryapi.dto.ReturnedLoanDTO;
import com.cursogabriel.libraryapi.model.entity.Book;
import com.cursogabriel.libraryapi.model.entity.Loan;
import com.cursogabriel.libraryapi.service.BookService;
import com.cursogabriel.libraryapi.service.Loanservice;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final Loanservice service;
    private final BookService bookService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create (@RequestBody LoanDTO dto) {
        Book book = bookService
                .getBookByIsbn(dto.getIsbn())
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed isbn"));

        Loan entity = Loan.builder()
                .book(book)
                .customer(dto.getCustomer())
                .localDate(LocalDate.now())
                .build();

        entity = service.save(entity);

        return entity.getId();
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void returnedBook(@PathVariable Long id, @RequestBody ReturnedLoanDTO dto) {
        Loan loan = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        loan.setReturned(dto.getReturned());
        service.update(loan);
    }

    @GetMapping
    public Page<LoanDTO> find(LoanFilterDTO dto, Pageable pageable) {

        Page<Loan> result = service.find(dto,pageable);
        List<LoanDTO> loans = result
                .getContent()
                .stream()
                .map( entity -> {
                    Book book = entity.getBook();
                    BookDTO bookDTO = modelMapper.map(book , BookDTO.class);
                    LoanDTO loanDTO = modelMapper.map(entity, LoanDTO.class);
                    loanDTO.setBook(bookDTO);
                    return loanDTO;
        }).collect(Collectors.toList());

        return new PageImpl<LoanDTO>(loans, pageable, result.getTotalElements());
    }
}
