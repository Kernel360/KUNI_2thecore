package com.example._thecore_back.car.exception;



public class CarNotFoundException extends RuntimeException {

    public CarNotFoundException(CarErrorCode carErrorCode, Object... args) {
        super(carErrorCode.format(args));
    }

}
