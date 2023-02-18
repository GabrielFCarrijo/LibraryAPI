package com.cursogabriel.libraryapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

public class Loan {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (length = 100)
    private String customer;
    @JoinColumn(name = "id_book")
    @ManyToOne
    private Book book;
    @Column
    private LocalDate localDate;
    @Column
    private Boolean returned;
}
