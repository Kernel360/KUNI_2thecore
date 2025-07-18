package com.example._thecore_back.car;

import com.example._thecore_back.car.application.CarService;
import com.example._thecore_back.car.domain.CarEntity;
import com.example._thecore_back.car.infrastructure.CarReaderImpl;
import com.example._thecore_back.car.infrastructure.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class CarRederImplTest {

    @Mock
    private CarRepository carRepository;

    private CarReaderImpl carReader;


    @BeforeEach
    public void setUp() {
        carReader = new CarReaderImpl(carRepository);
    }

    @Test
    @DisplayName("차량 번호를 조회")
    public void findCarByNumber() {

        var carEntity = CarEntity.builder()
                .carNumber("12가1234")
                .build();

        when(carRepository.findByCarNumber("12가1234")).thenReturn(Optional.of(carEntity));

        Optional<CarEntity> result = carReader.findByCarNumber("12가1234");

        assertTrue(result.isPresent());
        assertEquals("12가1234", result.get().getCarNumber());
    }

    @Test
    @DisplayName("등록되어 있는 전체 차량 조회")
    public void getAllCars(){
        List<CarEntity> carEntities = List.of(new CarEntity(),  new CarEntity());
        when(carRepository.findAll()).thenReturn(carEntities);

        List<CarEntity> result = carReader.findAll();

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("차량별 스테이터스 현황")
    public void getNumberByStatus(){

        List<Object[]> mockResult = List.of(new Object[]{"IN_USE", 1L}, new Object[]{"IDLE", 1L}, new Object[]{"MAINTENANCE", 1L});

        when(carRepository.getCountByStatus()).thenReturn(mockResult);

        List<Object[]> result = carReader.getCountByStatus();

        assertEquals(3, result.size());

        assertEquals("IN_USE", result.get(0)[0]);
        assertEquals(1L, result.get(0)[1]);

        assertEquals("IDLE", result.get(1)[0]);
        assertEquals(1L, result.get(1)[1]);

        assertEquals("MAINTENANCE", result.get(2)[0]);
        assertEquals(1L, result.get(2)[1]);

    }

    @Test
    @DisplayName("에뮬레이터 ID로 찾기")
    public void findByEmulatorId(){

        var carEntity = new CarEntity();
        carEntity.setEmulatorId(1);

        when(carRepository.findByEmulatorId(1)).thenReturn(Optional.of(carEntity));

        Optional<CarEntity> result = carReader.findByEmulatorId(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getEmulatorId());

    }
}
