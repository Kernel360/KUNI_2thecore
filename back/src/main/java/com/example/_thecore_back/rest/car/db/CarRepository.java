package com.example._thecore_back.rest.car.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import java.util.Optional;

public interface CarRepository extends JpaRepository<CarEntity, Integer> {

    Optional<CarEntity> findByCarNumber(String carNumber);

    Optional<CarEntity> findByEmulatorId(Integer emulatorId);

    @Query("select c.status, count(c) from Car c group by c.status")
    List<Object[]> getCountByStatus();


}
