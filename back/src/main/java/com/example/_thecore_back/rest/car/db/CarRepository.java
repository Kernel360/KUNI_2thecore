package com.example._thecore_back.rest.car.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<CarEntity, Integer> {

    Optional<CarEntity> findByCarNumber(String carNumber);

}
