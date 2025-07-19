package com.example._thecore_back.car;


import com.example._thecore_back.car.domain.CarEntity;
import com.example._thecore_back.car.infrastructure.CarRepository;
import com.example._thecore_back.car.infrastructure.CarWriterImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarWriterImplTest {

    @Mock
    private CarRepository carRepository;

    private CarWriterImpl carWriter;

    @BeforeEach
    public void setUp() {
        carWriter = new CarWriterImpl(carRepository);
    }

    @Test
    @DisplayName("차량 엔티티 생성")
    public void createCar(){

        var carEntity = new CarEntity();
        when(carRepository.save(carEntity)).thenReturn(carEntity);

        var result = carWriter.save(carEntity);

        assertEquals(carEntity, result);
        verify(carRepository).save(carEntity);

    }

    @Test
    @DisplayName("차량 삭제")
    public void deleteCar(){
        var carEntity = new CarEntity();

        carWriter.delete(carEntity);

        verify(carRepository, times(1)).delete(carEntity);
    }



}
