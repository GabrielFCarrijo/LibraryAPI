package com.cursogabriel.libraryapi.api.exeception;

import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.validation.BindingResult;

import javax.naming.Binding;
import java.util.ArrayList;
import java.util.List;

public class ApiErros {
    private List<String> errors;

    public ApiErros (BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(erros -> this.errors.add(erros.getDefaultMessage()));
    }

    public List<String> getErrors() {
        return errors;
    }
}
