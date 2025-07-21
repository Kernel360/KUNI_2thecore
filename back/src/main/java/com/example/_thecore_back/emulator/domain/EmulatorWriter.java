package com.example._thecore_back.emulator.domain;

public interface EmulatorWriter {
    EmulatorEntity save(EmulatorEntity emulator);
    void delete(EmulatorEntity emulator);
}
