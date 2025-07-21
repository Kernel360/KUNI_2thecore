package com.example._thecore_back.car.exception;

public class CarAlreadyExistsException extends RuntimeException {
    public CarAlreadyExistsException(String carNumber) {
        super(buildMessage(carNumber));
    }

    private static String buildMessage(String carNumber) {
            return String.format("이미 등록된 차량입니다: %s", carNumber);
    }
}
