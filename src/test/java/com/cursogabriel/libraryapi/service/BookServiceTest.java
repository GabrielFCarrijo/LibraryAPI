package com.cursogabriel.libraryapi.service;

import com.cursogabriel.libraryapi.exeption.BusinessException;
import com.cursogabriel.libraryapi.model.entity.Book;
import com.cursogabriel.libraryapi.model.repository.BookRepository;
import com.cursogabriel.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;
    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl( repository );
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        //cenario
        Book book = createValidBook();
        Mockito.when(repository.existsByISbn(Mockito.anyString())).thenReturn(false);
        Mockito.when( repository.save(book))
                .thenReturn(Book.builder().id(1l)
                        .isbn("123")
                        .author("Fulano De tal")
                        .title("fulaninhos").build()
                );

        // execucao
        Book savedBook = service.save(book);

        //verificação
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("fulaninhos");
        assertThat(savedBook.getAuthor()).isEqualTo("Fulano De tal");
    }

    private static Book createValidBook() {
        return Book.builder().isbn("123").author("Fulano De tal").title("fulaninhos").build();
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn ja cadastrado")
    public void sholdNotSaveBookDuplicatedIsbn() {

        //cenario
        Book book = createValidBook();
        Mockito.when(repository.existsByISbn(Mockito.anyString())).thenReturn(true);

        //execucao
        Throwable ex = Assertions.catchThrowable( () -> service.save(book));

        //verificacao
        assertThat(ex)
                .isInstanceOf(BusinessException.class)
                .hasMessage("ISBN ja cadastrada");

        Mockito.verify(repository, Mockito.never()).save(book);
    }
}
