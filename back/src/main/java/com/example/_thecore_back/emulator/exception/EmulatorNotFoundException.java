package com.example._thecore_back.emulator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmulatorNotFoundException extends RuntimeException {
    public EmulatorNotFoundException(String message) {
        super(message);
    }
}
