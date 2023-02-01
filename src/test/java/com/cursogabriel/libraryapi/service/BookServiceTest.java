package com.cursogabriel.libraryapi.service;

import com.cursogabriel.libraryapi.dto.BookDTO;
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

import java.util.Optional;

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

    private static Book createNewBook() {
        Book book = Book.builder().author("Pi").title("As Aventuras").isbn("001").build();
        return book;
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

    @Test
    @DisplayName("Deve obter um livro por id")
    public void getByIdTest() {
        //cenario
        Long id =1l;
        Book book = createValidBook();
        book.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        //execucao
        Optional<Book> foundBook = service.getById(id);

        //verificacoes
        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(id);
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());


    }

    @Test
    @DisplayName("Deve retornar vazio ao obter um livro pot id quando n existir livro na base")
    public void bookNotFoundByUdTest() {
        //cenario
        Long id =1l;

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //execucao
        Optional<Book> book = service.getById(id);

        //verificacoes
        assertThat(book.isPresent()).isFalse();

    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest () {
        // cenario
        Book book = Book.builder().id(1l).build();

        //execucao
        service.delete(book);

        //execucao
        Mockito.verify(repository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("Nao deve deletar um livro")
    public void notDeleteBookTest() {
        // cenario
        Book book = Book.builder().id(1l).build();

        //execucao
        service.delete(book);

        //execucao
        Mockito.verify(repository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("Nao deve atualizar um livro")
    public void notDeleteBook() {
        Book book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(book));

        Mockito.verify(repository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Deve fazer update de um livro")
    public void updatedBookTest () {
        //cenario
        Long id = 1l;

        //livro a atualizar
        Book updatingBook = Book.builder().id(id).build();

        //simulacao
        Book updetedBook = createValidBook();
        updetedBook.setId(id);
        Mockito.when(repository.save(updatingBook)).thenReturn(updetedBook);

        //execucao
        Book book = service.update(updatingBook);

        //verificacoes
        assertThat(book.getId()).isEqualTo(updetedBook.getId());
        assertThat(book.getIsbn()).isEqualTo(updetedBook.getIsbn());
        assertThat(book.getTitle()).isEqualTo(updetedBook.getTitle());
        assertThat(book.getAuthor()).isEqualTo(updetedBook.getAuthor());
    }

    @Test
    @DisplayName("Nao deve atualizar um livro")
    public void notUpdateBook() {
        Book book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(book));

        Mockito.verify(repository, Mockito.never()).save(book);
    }

}
