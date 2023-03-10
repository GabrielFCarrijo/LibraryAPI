package com.cursogabriel.libraryapi.model.repository;

import com.cursogabriel.libraryapi.model.entity.Book;
import com.cursogabriel.libraryapi.model.entity.Loan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query(value = "select case when ( count(l.id) > 0 ) then true else false end from Loan l where l.book = :book and l.returned is not true")
    @Bean(name="entityManagerFactory")
    boolean existsByBookAndNotReturnd(@Param("book") Book book);

    @Query(value = "select l from Loan as l join l.book as b where b.isbn = :isbn or l.customer = :customer ")
    Page<Loan> findByBookIsbnOrCustumer(
            @Param("isbn") String isbn,
            @Param("customer") String customer,
            Pageable pageable
    );

    Page<Loan> findByBook(Book book, Pageable pageable);
}
