package com.cursogabriel.libraryapi.model.entity;

import com.cursogabriel.libraryapi.service.BookService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

    private Long id;
    private String customer;
    private Book book;
    private LocalDate localDate;
    private Boolean returned;
}
