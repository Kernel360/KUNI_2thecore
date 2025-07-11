package com.example._thecore_back.rest.car.exception;

public class CarAlreadyExistsException extends RuntimeException {
    public CarAlreadyExistsException(String message) {
      super(message);
    }
}
