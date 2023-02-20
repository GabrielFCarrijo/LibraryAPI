package com.cursogabriel.libraryapi.resource;

import com.cursogabriel.libraryapi.dto.LoanDTO;
import com.cursogabriel.libraryapi.dto.LoanFilterDTO;
import com.cursogabriel.libraryapi.dto.ReturnedLoanDTO;
import com.cursogabriel.libraryapi.exeption.BusinessException;
import com.cursogabriel.libraryapi.model.entity.Book;
import com.cursogabriel.libraryapi.model.entity.Loan;
import com.cursogabriel.libraryapi.service.BookService;
import com.cursogabriel.libraryapi.service.LoanServiceTest;
import com.cursogabriel.libraryapi.service.Loanservice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = LoanController.class)
@AutoConfigureMockMvc
public class LoanControllerTest {

    static final String LOAN_API = "/api/loans";
    @Autowired
    MockMvc mvc;

    @MockBean
    BookService bookService;

    @MockBean
    Loanservice loanService;

    @Test
    @DisplayName("Deve realizar um emprestimo")
    public void createLoanTest() throws Exception {
        //Cenario
        LoanDTO dto = LoanDTO.builder().isbn("234").customer("Fulano").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        Book createdBook = Book.builder().id(1l).isbn("234").build();
        BDDMockito.given(bookService.getBookByIsbn("234"))
                .willReturn(Optional.of(createdBook));

        Loan loan = Loan.builder().id(1l).customer("Fulano").book(createdBook).localDate(LocalDate.now()).build();

        BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willReturn(loan);

        //Execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verificacao
        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("Deve retornar erro ao fazer emprestimo de um livro inexistente")
    public void invalidIsbnCreatedLoanTest() throws Exception {
        //Cenario
        LoanDTO dto = LoanDTO.builder().isbn("234").customer("Fulano").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(bookService.getBookByIsbn("234"))
                .willReturn(Optional.empty());


        //Execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verificacao
        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Book not found for passed isbn"));
    }

    @Test
    @DisplayName("Deve retornar erro ao fazer emprestimo de um livro ja emprestado")
    public void loneadBookCreatedLoanTest() throws Exception {
        //Cenario
        LoanDTO dto = LoanDTO.builder().isbn("234").customer("Fulano").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        Book createdBook = Book.builder().id(1l).isbn("234").build();
        BDDMockito.given(bookService.getBookByIsbn("234"))
                .willReturn(Optional.of(createdBook));

        BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willThrow(new BusinessException(("Book alredy loaned")));

        //Execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verificacao
        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Book alredy loaned"));
    }

    @Test
    @DisplayName("Deve retornar um livro")
    public void returnBookTest() throws Exception {
        //cenario( returned:true)
        ReturnedLoanDTO dto = ReturnedLoanDTO.builder().returned(true).build();
        Loan loan = Loan.builder().id(1l).build();
        BDDMockito.given(loanService.getById(Mockito.anyLong())).willReturn(Optional.of(loan));
        String json = new ObjectMapper().writeValueAsString(dto);


        mvc.perform(
                MockMvcRequestBuilders.patch(LOAN_API.concat("/1"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk() );

        Mockito.verify(loanService, Mockito.times(1)).update(loan);
    }

    @Test
    @DisplayName("Deve retornar 404 quando tentar devolver um livro inexistente")
    public void notreturnBookTest() throws Exception {
        //cenario( returned:true)
        ReturnedLoanDTO dto = ReturnedLoanDTO.builder().returned(true).build();
        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(loanService.getById(Mockito.anyLong())).willReturn(Optional.empty());


        mvc.perform(
                MockMvcRequestBuilders.patch(LOAN_API.concat("/1"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isNotFound() );
    }

    @Test
    @DisplayName("Deve filtrar emprestimos")
    public void findLoanTest() throws Exception {
        //cenario
        Long id = 1l;

        Loan loan = LoanServiceTest.createLoan();
        loan.setId(id);
        Book book = Book.builder().id(1l).isbn("321").build();
        loan.setBook(book);

        BDDMockito.given(loanService.find(Mockito.any(LoanFilterDTO.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<Loan>(Arrays.asList(loan), PageRequest.of(0, 10), 1));

        String queryString = String.format("?isbn=%s&customer=%s&page=0&size=10",
                loan.getBook().getIsbn(),
                loan.getCustomer());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LOAN_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(10))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }
}
