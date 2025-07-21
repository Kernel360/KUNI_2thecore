package com.example._thecore_back.emulator.application;

import com.example._thecore_back.rest.car.db.CarEntity;
import com.example._thecore_back.rest.car.db.CarRepository;
import com.example._thecore_back.emulator.domain.EmulatorEntity;
import com.example._thecore_back.emulator.domain.EmulatorStatus;
import com.example._thecore_back.emulator.controller.dto.EmulatorRequest;
import com.example._thecore_back.emulator.infrastructure.EmulatorRepository;
import com.example._thecore_back.emulator.exception.CarNotFoundException;
import com.example._thecore_back.emulator.exception.EmulatorNotFoundException;
import com.example._thecore_back.emulator.exception.DuplicateEmulatorException;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;


@ExtendWith(MockitoExtension.class)
public class EmulatorServiceTest {
    @Mock
    private EmulatorRepository emulatorRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private EmulatorService emulatorService;

    private EmulatorRequest emulatorRequest;
    private CarEntity carEntity;
    private EmulatorEntity emulatorEntity;

    @BeforeEach
    void setUp() {
        emulatorRequest = new EmulatorRequest();
        emulatorRequest.setCarNumber("123가 4567");

        carEntity = CarEntity.builder()
                .id(1)
                .carNumber("123가 4567")
                .build()
                ;
        emulatorEntity = EmulatorEntity.builder()
                .id(1L)
                .carNumber("123가 4567")
                .status(EmulatorStatus.OFF)
                .build()
                ;
    }

    @Test
    @DisplayName("애뮬레이터 등록 성공")
    void registerEmulator_success() {
        when(carRepository.findByCarNumber(anyString())).thenReturn(Optional.of(carEntity));
        when(emulatorRepository.save(any(EmulatorEntity.class))).thenReturn(emulatorEntity);
        when(carRepository.save(any(CarEntity.class))).thenReturn(carEntity);

        EmulatorEntity result = emulatorService.registerEmulator(emulatorRequest);

        assertNotNull(result);
        assertEquals("123가 4567", result.getCarNumber());
        assertEquals(EmulatorStatus.OFF, result.getStatus());

        carEntity.setEmulatorId(result.getId().intValue());
        verify(carRepository, times(1)).save(carEntity);
    }

    @Test
    @DisplayName("애뮬레이터 등록 실패 - 차량 없음")
    void registerEmulator_carNotFound() {
        when (carRepository.findByCarNumber(anyString())).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () ->
                emulatorService.registerEmulator(emulatorRequest));
        verify(emulatorRepository, never()).save(any(EmulatorEntity.class));
        verify(carRepository, never()).save(any(CarEntity.class));
    }

    @Test
    @DisplayName("애뮬레이터 등록 실패 - 해당 차량에 이미 애뮬레이터 연결됨")
    void registerEmulator_carAlreadyHasEmulator() {
        carEntity.setEmulatorId(1);
        when (carRepository.findByCarNumber(anyString())).thenReturn(Optional.of(carEntity));

        assertThrows(DuplicateEmulatorException.class, () ->
                emulatorService.registerEmulator(emulatorRequest));
        verify(emulatorRepository, never()).save(any(EmulatorEntity.class));
        verify(carRepository, never()).save(any(CarEntity.class));
    }

    @Test
    @DisplayName("애뮬레이터 상세 조회 성공")
    void getEmulator_success() {
        when (emulatorRepository.findById(anyLong())).thenReturn(Optional.of(emulatorEntity));

        EmulatorEntity result = emulatorService.getEmulator(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("123가 4567", result.getCarNumber());
    }

    @Test
    @DisplayName("애뮬레이터 상세 조회 실패 - 애뮬레이터 없음")
    void getEmulator_notFound() {
        when (emulatorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EmulatorNotFoundException.class, () ->
                emulatorService.getEmulator(1L));
    }

    @Test
    @DisplayName("애뮬레이터 전체 조회 성공")
    void getAllEmulators_success() {
        List<EmulatorEntity> emulators =
                Arrays.asList(emulatorEntity,
                        EmulatorEntity.builder()
                                .id(2L)
                                .carNumber("789나 0123")
                                .status(EmulatorStatus.ON)
                                .build());

        when (emulatorRepository.findAll()).thenReturn(emulators);

        List<EmulatorEntity> result = emulatorService.getAllEmulators();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("123가 4567", result.get(0).getCarNumber());
        assertEquals("789나 0123", result.get(1).getCarNumber());
    }

    @Test
    @DisplayName("애뮬레이터 전체 조회 성공 - 애뮬레이터가 없을 때 빈 리스트 반환")
    void getAllEmulators_emptyList() {
        when (emulatorRepository.findAll()).thenReturn(Collections.emptyList());

        List<EmulatorEntity> result = emulatorService.getAllEmulators();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("애뮬레이터 수정 성공 - 차량 번호 변경 없을 때")
    void updateEmulator_noCarNumberChange_success() {
        EmulatorRequest updatedRequest = new EmulatorRequest();
        updatedRequest.setCarNumber("123가 4567");

        when (emulatorRepository.findById(anyLong())).thenReturn(Optional.of(emulatorEntity));
        when (emulatorRepository.save(any(EmulatorEntity.class))).thenReturn(emulatorEntity);

        EmulatorEntity result = emulatorService.updateEmulator(1L, updatedRequest);

        assertNotNull(result);
        assertEquals("123가 4567", result.getCarNumber());
        verify(carRepository, never()).findByCarNumber(anyString());
        verify(carRepository, never()).save(any(CarEntity.class));
    }

    @Test
    @DisplayName("애뮬레이터 수정 성공 - 차량 번호 변경 있을 때")
    void updateEmulator_carNumberChange_success() {
        EmulatorRequest updatedRequest = new EmulatorRequest();
        updatedRequest.setCarNumber("새로운 차량 번호");

        CarEntity oldCarEntity = CarEntity.builder()
                .id(1)
                .carNumber("123가 4567")
                .emulatorId(1)
                .build()
                ;
        CarEntity newCarEntity = CarEntity.builder()
                .id(2)
                .carNumber("새로운 차량 번호")
                .build()
                ;

        when (emulatorRepository.findById(anyLong())).thenReturn(Optional.of(emulatorEntity));
        when (carRepository.findByCarNumber("새로운 차량 번호")).thenReturn(Optional.of(newCarEntity));
        when (carRepository.findByCarNumber("123가 4567")).thenReturn(Optional.of(oldCarEntity));
        when (emulatorRepository.findByCarNumber("새로운 차량 번호")).thenReturn(Optional.empty());
        when (emulatorRepository.save(any(EmulatorEntity.class))).thenReturn(emulatorEntity);

        EmulatorEntity result = emulatorService.updateEmulator(1L, updatedRequest);

        assertNotNull(result);
        assertEquals("새로운 차량 번호", result.getCarNumber());
        assertNull(oldCarEntity.getEmulatorId());
        assertEquals(1, newCarEntity.getEmulatorId());
        verify(carRepository, times(2)).save(any(CarEntity.class));
    }

    @Test
    @DisplayName("애뮬레이터 수정 실패 - 애뮬레이터 없음")
    void updateEmulator_emulatorNotFound() {
        when(emulatorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EmulatorNotFoundException.class, () ->
                emulatorService.updateEmulator(1L, emulatorRequest));
    }

    @Test
    @DisplayName("애뮬레이터 수정 실패 - 변경할 차량 없음")
    void updateEmulator_newCarNotFound() {
        EmulatorRequest updatedRequest = new EmulatorRequest();
        updatedRequest.setCarNumber("새로운 차량 번호");

        when (emulatorRepository.findById(anyLong())).thenReturn(Optional.of(emulatorEntity));
        when (carRepository.findByCarNumber("새로운 차량 번호")).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () ->
                emulatorService.updateEmulator(1L, updatedRequest));
        verify(emulatorRepository, never()).save(any(EmulatorEntity.class));
        verify(carRepository, never()).save(any(CarEntity.class));
    }

    @Test
    @DisplayName(("애뮬레이터 수정 실패 - 변경할 차량에 이미 애뮬레이터 연결됨"))
    void updateEmulator_newCarAlreadyHasEmulator() {
        EmulatorRequest updatedRequest = new EmulatorRequest();
        updatedRequest.setCarNumber("새로운 차량 번호");

        CarEntity newCarEntityWithEmulator = CarEntity.builder()
                .id(2)
                .carNumber("새로운 차량 번호")
                .emulatorId(5)
                .build()
                ;

        when (emulatorRepository.findById(anyLong())).thenReturn(Optional.of(emulatorEntity));
        when (carRepository.findByCarNumber("새로운 차량 번호")).thenReturn(Optional.of(newCarEntityWithEmulator));

        assertThrows(DuplicateEmulatorException.class, () ->
                emulatorService.updateEmulator(1L, updatedRequest));
        verify(emulatorRepository, never()).save(any(EmulatorEntity.class));
        verify(carRepository, never()).save(any(CarEntity.class));
    }

    @Test
    @DisplayName("애뮬레이터 수정 실패 - 변경할 carNumber에 이미 애뮬레이터 존재")
    void updateEmulator_duplicateEmulatorOnNewCarNumber() {
        EmulatorRequest updatedRequest = new EmulatorRequest();
        updatedRequest.setCarNumber("새로운 차량 번호");

        CarEntity newCarEntity = CarEntity.builder()
                .id(2)
                .carNumber("새로운 차량 번호")
                .build()
                ;
        EmulatorEntity existingEmulatorOnNewCar = EmulatorEntity.builder()
                .id(3L)
                .carNumber("새로운 차량 번호")
                .build()
                ;

        when (emulatorRepository.findById(anyLong())).thenReturn(Optional.of(emulatorEntity));
        when (carRepository.findByCarNumber("새로운 차량 번호")).thenReturn(Optional.of(newCarEntity));
        when (emulatorRepository.findByCarNumber("새로운 차량 번호")).thenReturn(Optional.of(existingEmulatorOnNewCar));

        assertThrows(DuplicateEmulatorException.class, () ->
                emulatorService.updateEmulator(1L, updatedRequest));
        verify (emulatorRepository, never()).save(any(EmulatorEntity.class));
        verify (carRepository, never()).save(any(CarEntity.class));
    }

    @Test
    @DisplayName("애뮬레이터 삭제 성공")
    void deleteEmulator_success() {
            carEntity.setEmulatorId(1);

            when (emulatorRepository.findById(anyLong())).thenReturn(Optional.of(emulatorEntity));
            when (carRepository.findByCarNumber(anyString())).thenReturn(Optional.of(carEntity));
            doNothing().when (emulatorRepository).deleteById(anyLong());

            emulatorService.deleteEmulator(1L);

            assertNull(carEntity.getEmulatorId());
            verify(carRepository, times(1)).save(carEntity);
            verify (emulatorRepository, times(1)).deleteById(1L);

    }

    @Test
    @DisplayName("애뮬레이터 삭제 실패 - 애뮬레이터 없음")
    void deleteEmulator_notFound() {
        when (emulatorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EmulatorNotFoundException.class, () ->
                emulatorService.deleteEmulator(1L));
        verify(emulatorRepository, never()).deleteById(anyLong());
        verify(carRepository, never()).save(any(CarEntity.class));

    }
}
