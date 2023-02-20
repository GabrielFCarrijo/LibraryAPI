package com.cursogabriel.libraryapi.repository;

import com.cursogabriel.libraryapi.model.entity.Book;
import com.cursogabriel.libraryapi.model.entity.Loan;
import com.cursogabriel.libraryapi.model.repository.LoanRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;
import javax.swing.*;
import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@EntityScan(basePackages = "io.summer")
public class LoanRepositoryTest {


    private static Book createNewBook(String isbn) {
        Book book = Book.builder().author("Pi").title("As Aventuras").isbn(isbn).build();
        return book;
    }

    @Autowired
    private LoanRepository repository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    @DisplayName("Deve verificar se existe emprestimo nao devolvido para o livro")
    public void existsByBookAndNotReturnd () {
        //cenario
        Book book = createNewBook("123");
        entityManager.persist(book);
        Loan loan = Loan.builder().book(book).customer("Fulano").localDate(LocalDate.now()).build();
        entityManager.persist(loan);

        //execucao
        boolean exists = repository.existsByBookAndNotReturnd(book);

        //verificacao
        assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("Deve buscar um emprestimo pelo isbn do livro ou customer")
    public void findByBookIsbnOrCustumer() {

        //cenario
        Book book = createNewBook("123");
        entityManager.persist(book);
        Loan loan = Loan.builder().book(book).customer("Fulano").localDate(LocalDate.now()).build();
        entityManager.persist(loan);

        Page<Loan> result = repository.findByBookIsbnOrCustumer("123", "fulano", PageRequest.of(0,10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}
