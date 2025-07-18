package com.example._thecore_back.rest.emulator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEmulatorException extends RuntimeException {
    public DuplicateEmulatorException(String message) {
        super(message);
    }
}
