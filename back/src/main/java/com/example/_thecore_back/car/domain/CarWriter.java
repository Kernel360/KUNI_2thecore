package com.example._thecore_back.car.domain;

public interface CarWriter {
    CarEntity save(CarEntity carEntity);

    void delete(CarEntity carEntity);
}
