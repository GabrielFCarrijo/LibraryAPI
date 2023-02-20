package com.cursogabriel.libraryapi.service;

import com.cursogabriel.libraryapi.exeption.BusinessException;
import com.cursogabriel.libraryapi.model.entity.Book;
import com.cursogabriel.libraryapi.model.entity.Loan;
import com.cursogabriel.libraryapi.model.repository.LoanRepository;
import com.cursogabriel.libraryapi.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    Loanservice service;
    @MockBean
    LoanRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new LoanServiceImpl(repository);
    }

    public Loan createLoan(){
        Book book = Book.builder().id(1l).build();
        String customer = "Fulano";

        return Loan.builder()
                .book(book)
                .customer(customer)
                .localDate(LocalDate.now())
                .build();
    }
    @Test
    @DisplayName("Deve salvar um emprentimo")
    public void saveLoanTest() {
        Book book = Book.builder().id(1l).build();
        String customer = "Fulano";

        Loan savingLoan = Loan.builder()
                .book(book)
                .customer(customer)
                .localDate(LocalDate.now())
                .build();

        Loan savedLoan = Loan.builder()
                .id(1l)
                .customer(customer)
                .localDate(LocalDate.now())
                .book(book)
                .build();

        Mockito.when(repository.existsByBookAndNotReturnd(book)).thenReturn(false);
        Mockito.when(repository.save(savingLoan)).thenReturn(savedLoan);

        Loan loan = service.save(savingLoan);

        assertThat(loan.getId()).isEqualTo(savedLoan.getId());
        assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
        assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
        assertThat(loan.getLocalDate()).isEqualTo(savedLoan.getLocalDate());
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao salvar um emprestimo com livro ja emprestado")
    public void loanedBookSaveTest() {
        Book book = Book.builder().id(1l).build();
        String customer = "Fulano";

        Loan savingLoan = Loan.builder()
                .book(book)
                .customer(customer)
                .localDate(LocalDate.now())
                .build();

        Mockito.when(repository.existsByBookAndNotReturnd(book)).thenReturn(true);

        Throwable ex = catchThrowable(() -> service.save(savingLoan));

        assertThat(ex).isInstanceOf(BusinessException.class).hasMessage("Book alredy loaned");

        Mockito.verify(repository, Mockito.never()).save(savingLoan);
    }

    @Test
    @DisplayName("Deve obter as informações de um emprestimo pelo id")
    public void getLoanDetails() {
        //cenario
        Long id = 1l;
        Loan loan = createLoan();
        loan.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(loan));

        //execucao
        Optional<Loan> result = service.getById(id);

        //verificacao
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getCustomer()).isEqualTo(loan.getCustomer());
        assertThat(result.get().getBook()).isEqualTo(loan.getBook());
        assertThat(result.get().getLocalDate()).isEqualTo(loan.getLocalDate());

        Mockito.verify(repository).findById(id);
    }
}
