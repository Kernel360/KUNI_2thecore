package com.example._thecore_back.car.infrastructure;

import com.example._thecore_back.car.domain.CarEntity;
import com.example._thecore_back.car.domain.CarWriter;
import org.springframework.stereotype.Repository;

@Repository
public class CarWriterImpl implements CarWriter {
    private final CarRepository carRepository;

    public CarWriterImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public CarEntity save(CarEntity carEntity){
        return carRepository.save(carEntity);
    }

    public void delete(CarEntity carEntity){
        carRepository.delete(carEntity);
    }
}
