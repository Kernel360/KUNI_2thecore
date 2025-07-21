package com.example._thecore_back.car;

import com.example._thecore_back.car.application.CarService;
import com.example._thecore_back.car.controller.dto.CarDeleteDto;
import com.example._thecore_back.car.controller.dto.CarDetailDto;
import com.example._thecore_back.car.controller.dto.CarRequestDto;
import com.example._thecore_back.car.domain.CarEntity;
import com.example._thecore_back.car.domain.CarStatus;
import com.example._thecore_back.car.exception.CarAlreadyExistsException;
import com.example._thecore_back.car.exception.CarNotFoundException;
import com.example._thecore_back.car.infrastructure.CarReaderImpl;
import com.example._thecore_back.car.infrastructure.CarWriterImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    private CarReaderImpl carReader;

    @Mock
    private CarWriterImpl carWriter;

    @InjectMocks
    private CarService carService;

    @Test
    @DisplayName("createCar Test - success")
    void createCarSuccess() {
        // 요청 객체 생성
        CarRequestDto request = CarRequestDto.builder()
                .brand("현대")
                .model("아반떼")
                .carYear(2025)
                .carType("중형")
                .carNumber("12가3456")
                .sumDist(1234.56)
                .build();

        // 저장될 Entity 생성
        CarEntity carEntity = CarEntity.builder()
                .brand("현대")
                .model("아반떼")
                .carYear(2025)
                .carType("중형")
                .carNumber("12가3456")
                .sumDist(1234.56)
                .status(CarStatus.IDLE)
                .build();

        when(carReader.findByCarNumber("12가3456")).thenReturn(Optional.empty());
        when(carWriter.save(any())).thenReturn(carEntity);

        // when
        CarDetailDto result = carService.createCar(request);

        // then
        assertNotNull(result);
        assertEquals("현대", result.getBrand());
        assertEquals("아반떼", result.getModel());
        assertEquals(2025, result.getCarYear());
        assertEquals("중형", result.getCarType());
        assertEquals("12가3456", result.getCarNumber());
        assertEquals(1234.56, result.getSumDist(), 0);
        assertEquals("대기", result.getStatus());
    }

    @Test
    @DisplayName("updateCar Test - success")
    void updateCarSuccess() {
        // 요청 객체 생성
        CarRequestDto request = CarRequestDto.builder()
                .carYear(2015)
                .status("운행")
                .build();

        // 저장될 Entity 생성
        CarEntity carEntity = CarEntity.builder()
                .brand("현대")
                .model("아반떼")
                .carYear(2015)
                .carType("중형")
                .carNumber("12가3456")
                .sumDist(1234.56)
                .status(CarStatus.IN_USE)
                .build();

        when(carReader.findByCarNumber("12가3456")).thenReturn(Optional.of(carEntity));
        when(carWriter.save(any())).thenReturn(carEntity);

        // when
        CarDetailDto result = carService.updateCar(request, "12가3456");

        // then
        assertNotNull(result);
        assertEquals("현대", result.getBrand());
        assertEquals("아반떼", result.getModel());
        assertEquals(2015, result.getCarYear());
        assertEquals("중형", result.getCarType());
        assertEquals("12가3456", result.getCarNumber());
        assertEquals(1234.56, result.getSumDist(), 0);
        assertEquals("운행", result.getStatus());
    }

    @Test
    @DisplayName("deleteCar Test - success")
    void deleteCarSuccess() {
        // 저장될 Entity 생성
        CarEntity carEntity = CarEntity.builder()
                .brand("현대")
                .model("아반떼")
                .carYear(2025)
                .carType("중형")
                .carNumber("12가3456")
                .sumDist(1234.56)
                .status(CarStatus.IDLE)
                .build();

        when(carReader.findByCarNumber("12가3456")).thenReturn(Optional.of(carEntity));
        doNothing().when(carWriter).delete(any());

        // when
        CarDeleteDto result = carService.deleteCar("12가3456");

        // then
        assertNotNull(result);
        assertEquals("현대", result.getBrand());
        assertEquals("아반떼", result.getModel());
        assertEquals("12가3456", result.getCarNumber());
    }

    @Test
    @DisplayName("createCar Test - fail: 중복된 차량 번호 존재")
    void createCarFail() {
        // 요청 객체 생성
        CarRequestDto request = CarRequestDto.builder()
                .brand("현대")
                .model("아반떼")
                .carYear(2025)
                .carType("중형")
                .carNumber("12가3456")
                .sumDist(1234.56)
                .build();

        // 저장될 Entity 생성
        CarEntity carEntity = CarEntity.builder()
                .brand("현대")
                .model("아반떼")
                .carYear(2025)
                .carType("중형")
                .carNumber("12가3456")
                .sumDist(1234.56)
                .status(CarStatus.IDLE)
                .build();

        when(carReader.findByCarNumber("12가3456")).thenReturn(Optional.of(carEntity));

        // when & then
        assertThrows(CarAlreadyExistsException.class, () -> {
            carService.createCar(request);
        });
    }

    @Test
    @DisplayName("updateCar Test - fail: 존재하지 않는 챠량 번호")
    void updateCarFail() {
        // 요청 객체 생성
        CarRequestDto request = CarRequestDto.builder()
                .carYear(2015)
                .status("운행")
                .build();

        when(carReader.findByCarNumber("12가3456")).thenReturn(Optional.empty());

        // when & then
        assertThrows(CarNotFoundException.class, () -> {
            carService.updateCar(request, "12가3456");
        });
    }

    @Test
    @DisplayName("updateCar Test - fail: 이미 존재하는 차량 번호")
    void updateCarFailAlreadyExists() {
        String originalCarNumber = "12가3456"; // 기존 차량
        String duplicateCarNumber = "99가9999"; // 중복 번호

        // 요청 객체 생성
        CarRequestDto request = CarRequestDto.builder()
                .carYear(2015)
                .status("운행")
                .carNumber(duplicateCarNumber)
                .build();

        CarEntity alreadyExistingCar = CarEntity.builder()
                .carNumber(duplicateCarNumber)
                .build();

        CarEntity targetCar = CarEntity.builder()
                .carNumber(originalCarNumber)
                .build();

        when(carReader.findByCarNumber(originalCarNumber)).thenReturn(Optional.of(targetCar));
        when(carReader.findByCarNumber(duplicateCarNumber)).thenReturn(Optional.of(alreadyExistingCar));

        // when & then
        assertThrows(CarAlreadyExistsException.class, () -> {
            carService.updateCar(request, originalCarNumber);
        });
    }

    @Test
    @DisplayName("deleteCar Test - fail: 존재하지 않는 차량 번호 조회")
    void deleteCarFail() {

        when(carReader.findByCarNumber("12가3456")).thenReturn(Optional.empty());

        // when & then
        assertThrows(CarNotFoundException.class, () -> {
            carService.deleteCar("12가3456");
        });
    }
}
