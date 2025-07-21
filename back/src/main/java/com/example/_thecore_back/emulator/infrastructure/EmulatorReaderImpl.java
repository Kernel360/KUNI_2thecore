package com.example._thecore_back.emulator.infrastructure;

import com.example._thecore_back.emulator.domain.EmulatorEntity;
import com.example._thecore_back.emulator.domain.EmulatorReader;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmulatorReaderImpl implements EmulatorReader {
    private final EmulatorRepository emulatorRepository;

    public EmulatorReaderImpl(EmulatorRepository emulatorRepository) {
        this.emulatorRepository = emulatorRepository;
    }

    @Override
    public Optional<EmulatorEntity> findById(Long id) {
        return emulatorRepository.findById(id);
    }

    @Override
    public List<EmulatorEntity> findAll() {
        return emulatorRepository.findAll();
    }
}
