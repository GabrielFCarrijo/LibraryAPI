package com.cursogabriel.libraryapi.repository;

import com.cursogabriel.libraryapi.model.entity.Book;
import com.cursogabriel.libraryapi.model.repository.LoanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    private LoanRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private static Book createNewBook(String isbn) {
        Book book = Book.builder().author("Pi").title("As Aventuras").isbn(isbn).build();
        return book;
    }

    @Test
    @DisplayName("Deve verificar se existe emprestimo nao devolvido para o livro")
    public void existsByBookAndNotReturnd () {

        Book book = createNewBook("123");

        entityManager().persist(book);
        entityManager().persist(loan);

        repository.existsByBookAndNotReturnd(book);

    }

    private TestEntityManager entityManager() {
        return null;
    }
}
