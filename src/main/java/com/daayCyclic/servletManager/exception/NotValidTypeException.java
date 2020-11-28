package com.daayCyclic.servletManager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotValidTypeException extends RuntimeException {

    public NotValidTypeException() {
        super("Not a valid type.");
    }

    public NotValidTypeException(String message) {
        super(message);
    }
}
