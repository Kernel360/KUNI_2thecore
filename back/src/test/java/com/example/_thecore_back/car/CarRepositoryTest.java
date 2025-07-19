package com.example._thecore_back.car;

import com.example._thecore_back.car.domain.CarEntity;
import com.example._thecore_back.car.domain.CarStatus;
import com.example._thecore_back.car.infrastructure.CarRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository.deleteAll();
    }


    @Test
    @DisplayName("차량 번호를 이용하여 검색이 성공")
    public void findByCarNumber(){

        var carEntity = CarEntity.builder()
                        .model("아이오닉")
                        .brand("현대").carYear(2023)
                        .status(CarStatus.IDLE).carType("소형")
                        .carNumber("12가1234").emulatorId(1)
                        .build();

        carRepository.save(carEntity);

        Optional<CarEntity> result = carRepository.findByCarNumber(carEntity.getCarNumber());

        assertTrue(result.isPresent());
        assertEquals(carEntity.getCarNumber(), result.get().getCarNumber());

    }

    @Test
    @DisplayName("해당 차량이 존재하지 않음")
    public void findByCarNumberFailed(){

        var carEntity = CarEntity.builder()
                .model("아이오닉")
                .brand("현대").carYear(2023)
                .status(CarStatus.IDLE).carType("소형")
                .carNumber("12가1234").emulatorId(1)
                .build();

        carRepository.save(carEntity);

        Optional<CarEntity> result = carRepository.findByCarNumber("12233");

        assertTrue(result.isEmpty());

    }

    @Test
    @DisplayName("전체 차량 조회")
    public void getCars(){



        var carEntity = CarEntity.builder()
                .model("아이오닉")
                .brand("현대").carYear(2023)
                .status(CarStatus.IDLE).carType("소형")
                .carNumber("12가1234").emulatorId(1)
                .build();

        carRepository.save(carEntity);

        List<CarEntity> result = carRepository.findAll();

        assertEquals(1, result.size());


    }

    @Test
    @DisplayName("status별 차량 조회")
    public void getCarsByFilter(){

        List<CarEntity> cars = List.of(
            CarEntity.builder()
                    .model("아이오닉")
                    .brand("현대").carYear(2023)
                    .status(CarStatus.IDLE).carType("소형")
                    .carNumber("12가1234").emulatorId(1)
                    .build(),

            CarEntity.builder()
                    .model("그렌져")
                    .brand("기아").carYear(2024)
                    .status(CarStatus.IN_USE).carType("소형")
                    .carNumber("12가3423").emulatorId(2)
                    .build(),

                    CarEntity.builder()
                            .model("마티스")
                            .brand("기아").carYear(2024)
                            .status(CarStatus.IN_USE).carType("소형")
                            .carNumber("12가3453").emulatorId(3)
                            .build());

        carRepository.saveAll(cars);

        List<Object[]> carStatusList = carRepository.getCountByStatus();

        assertEquals(2, carStatusList.size());

        assertEquals(CarStatus.IDLE, carStatusList.get(0)[0]);
        assertEquals(1L, carStatusList.get(0)[1]);

        assertEquals(CarStatus.IN_USE, carStatusList.get(1)[0]);
        assertEquals(2L, carStatusList.get(1)[1]);


        }
    @Test
    @DisplayName("새로운 차량 생성 후 삽입")
    public void createCar(){

        var entity = CarEntity.builder()
                .model("아이오닉")
                .brand("현대").carYear(2023)
                .status(CarStatus.IDLE).carType("소형")
                .carNumber("12가1234").emulatorId(1)
                .build();

        carRepository.save(entity);

        assertEquals(1, carRepository.count());
        assertTrue(carRepository.findByCarNumber("12가1234").isPresent());

    }

    @Test
    @DisplayName("차량 삭제")
    public void deleteCar(){

        var entity = CarEntity.builder()
                .model("아이오닉")
                .brand("현대").carYear(2023)
                .status(CarStatus.IDLE).carType("소형")
                .carNumber("12가1234").emulatorId(1)
                .build();

        carRepository.save(entity);

        assertEquals("12가1234",carRepository.findByCarNumber(entity.getCarNumber()).get().getCarNumber());

        carRepository.delete(entity);

        Optional<CarEntity> secondResult = carRepository.findByCarNumber("12가1234");
        assertTrue(secondResult.isEmpty());

    }



}
