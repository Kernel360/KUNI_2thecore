package com.example.common.infrastructure.car;

import com.example.common.domain.car.CarEntity;
import com.example.common.domain.car.CarReader;
import com.example.common.domain.car.CarStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "car.reader.db.enabled", havingValue = "true", matchIfMissing = true)

public class CarReaderImpl implements CarReader {

    private final CarRepository carRepository;

    public CarReaderImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Optional<CarEntity> findByCarNumber(String carNumber){
        return carRepository.findByCarNumber(carNumber);
    }

//    public List<CarEntity> findAll(){
//        return carRepository.findAll();
//    }

    public Page<CarEntity> findAll(Pageable pageable){
        return carRepository.findAll(pageable);
    }

    public Map<CarStatus, Long> getCountByStatus(){

        List<Object[]> result = carRepository.getCountByStatus();

        return result.stream().collect(Collectors.toMap(
                row -> (CarStatus) row[0],
                row -> (Long) row[1]
        ));

    }


    @Override
    public List<CarEntity> findByStatus(List<CarStatus> statuses) {
        return carRepository.findByStatusIn(statuses);
    }
}