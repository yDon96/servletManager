package com.daayCyclic.servletManager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateGenerationException extends RuntimeException {

    public DuplicateGenerationException() {
        super("Trying to generate an object that already exist.");
    }

    public DuplicateGenerationException(String message) {
        super(message);
    }
}
