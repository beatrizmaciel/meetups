package com.bibs.meetups.controller.exceptions;

import com.bibs.meetups.exception.BusinessException;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    private final List<String> errors;

    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>(); // construtor
        bindingResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));

    }

    public ApiErrors(BusinessException e) {
        this.errors = Arrays.asList(e.getMessage());
    }

    public ApiErrors(ResponseStatusException e) {
        this.errors = Arrays.asList(e.getReason());
    }

    public List<String> getErrors() {
        return errors;
    }


}

// aqui concentram-se os tipos de exceções e seus tratamentos