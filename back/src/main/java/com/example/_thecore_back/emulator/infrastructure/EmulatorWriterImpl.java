package com.example._thecore_back.emulator.infrastructure;

import com.example._thecore_back.emulator.domain.EmulatorEntity;
import com.example._thecore_back.emulator.domain.EmulatorWriter;
import org.springframework.stereotype.Repository;

@Repository
public class EmulatorWriterImpl implements EmulatorWriter {
    private final EmulatorRepository emulatorRepository;

    public EmulatorWriterImpl(EmulatorRepository emulatorRepository) {
        this.emulatorRepository = emulatorRepository;
    }

    @Override
    public EmulatorEntity save(EmulatorEntity emulator) {
        return emulatorRepository.save(emulator);
    }

    @Override
    public void delete(EmulatorEntity emulator) {
        emulatorRepository.delete(emulator);
    }
}
