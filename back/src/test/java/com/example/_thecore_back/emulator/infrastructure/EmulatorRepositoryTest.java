package com.example._thecore_back.emulator.infrastructure;

import com.example._thecore_back.emulator.domain.EmulatorEntity;
import com.example._thecore_back.emulator.domain.EmulatorStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class EmulatorRepositoryTest {

    @Autowired
    private EmulatorRepository emulatorRepository;

    @Test
    @DisplayName("차량 번호로 애뮬레이터 조회")
    void findByCarNumber() {
        // given
        EmulatorEntity emulator = EmulatorEntity.builder()
                .carNumber("123가4567")
                .status(EmulatorStatus.OFF)
                .build();
        emulatorRepository.save(emulator);

        // when
        Optional<EmulatorEntity> foundEmulator = emulatorRepository.findByCarNumber("123가4567");

        // then
        assertTrue(foundEmulator.isPresent());
        assertEquals("123가4567", foundEmulator.get().getCarNumber());
    }

    @Test
    @DisplayName("존재하지 않는 차량 번호로 애뮬레이터 조회")
    void findByCarNumber_notFound() {
        // when
        Optional<EmulatorEntity> foundEmulator = emulatorRepository.findByCarNumber("000가0000");

        // then
        assertFalse(foundEmulator.isPresent());
    }

    @Test
    @DisplayName("애뮬레이터 저장 및 삭제")
    void saveAndDeleteEmulator() {
        EmulatorEntity emulator = EmulatorEntity.builder()
                .carNumber("555하7777")
                .status(EmulatorStatus.ON)
                .build();

        EmulatorEntity saved = emulatorRepository.save(emulator);
        assertNotNull(saved.getId());

        emulatorRepository.deleteById(saved.getId());
        assertFalse(emulatorRepository.findById(saved.getId()).isPresent());
    }

    @Test
    @DisplayName("전체 애뮬레이터 조회")
    void findAllEmulators() {
        emulatorRepository.save(EmulatorEntity.builder()
                .carNumber("111가2222")
                .status(EmulatorStatus.OFF)
                .build());

        emulatorRepository.save(EmulatorEntity.builder()
                .carNumber("333나4444")
                .status(EmulatorStatus.ON)
                .build());

        var all = emulatorRepository.findAll();
        assertEquals(2, all.size());
    }
}
