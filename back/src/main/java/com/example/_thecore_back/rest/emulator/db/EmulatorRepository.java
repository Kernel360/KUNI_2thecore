package com.example._thecore_back.rest.emulator.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmulatorRepository extends JpaRepository<EmulatorEntity, Long> {
    Optional<EmulatorEntity> findByCarNumber(String carNumber);
}
