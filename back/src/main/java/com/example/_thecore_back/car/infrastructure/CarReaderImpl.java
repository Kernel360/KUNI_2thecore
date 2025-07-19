package com.example._thecore_back.car.infrastructure;

import com.example._thecore_back.car.domain.CarEntity;
import com.example._thecore_back.car.domain.CarReader;
import com.example._thecore_back.car.domain.CarStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CarReaderImpl implements CarReader {

    private final CarRepository carRepository;

    public CarReaderImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Optional<CarEntity> findByCarNumber(String carNumber){
        return carRepository.findByCarNumber(carNumber);
    }

    public List<CarEntity> findAll(){
        return carRepository.findAll();
    }

    public Map<CarStatus, Long> getCountByStatus(){

        List<Object[]> result = carRepository.getCountByStatus();

        return result.stream().collect(Collectors.toMap(
                row -> (CarStatus) row[0],
                row -> (Long) row[1]
        ));

    }

    public Optional<CarEntity> findByEmulatorId(Integer emulatorId){
        return carRepository.findByEmulatorId(emulatorId);
    }
}
