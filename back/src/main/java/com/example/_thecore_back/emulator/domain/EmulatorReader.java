package com.example._thecore_back.emulator.domain;

import java.util.List;
import java.util.Optional;

public interface EmulatorReader {
    Optional<EmulatorEntity> findById(Long id);
    List<EmulatorEntity> findAll();
}
