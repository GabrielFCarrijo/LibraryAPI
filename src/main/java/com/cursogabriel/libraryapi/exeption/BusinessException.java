package com.cursogabriel.libraryapi.exeption;

public class BusinessException extends RuntimeException {
    public BusinessException(String isbnJaCadastrada) {
        super(isbnJaCadastrada);
    }
}
