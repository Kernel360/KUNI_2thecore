package com.example._thecore_back.rest.car.service;


import com.example._thecore_back.rest.car.exception.CarAlreadyExistsException;
import com.example._thecore_back.rest.car.model.dto.CarDeleteDto;
import com.example._thecore_back.rest.car.model.dto.CarDetailDto;
import com.example._thecore_back.rest.car.model.dto.CarSearchDto;
import com.example._thecore_back.rest.car.model.dto.CarSummaryDto;
import com.example._thecore_back.rest.car.exception.CarNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example._thecore_back.rest.car.db.CarEntity;
import com.example._thecore_back.rest.car.db.CarRepository;
import com.example._thecore_back.rest.car.db.CarStatus;
import com.example._thecore_back.rest.car.model.dto.CarRequestDto;


@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public CarDetailDto getCar(String carNumber){
        var entity =  carRepository.findByCarNumber(carNumber).orElseThrow(() -> new CarNotFoundException(carNumber));
        return CarDetailDto.EntityToDto(entity);
    }

    public List<CarSearchDto> getAllCars(){

        return carRepository.findAll().stream()
                .map(CarSearchDto::EntityToDto).collect(Collectors.toList());

    }

    public CarSummaryDto getCountByStatus(){

        Map<CarStatus, Long> result = carRepository.getCountByStatus().stream().collect(Collectors.toMap(
                row -> (CarStatus) row[0],
                row -> (Long) row[1]
        ));

        return CarSummaryDto.builder()
                .operating(result.getOrDefault(CarStatus.IN_USE, 0L))
                .waiting(result.getOrDefault(CarStatus.IDLE, 0L))
                .inspecting(result.getOrDefault(CarStatus.MAINTENANCE, 0L))
                .total(result.getOrDefault(CarStatus.IN_USE, 0L) +
                        result.getOrDefault(CarStatus.IDLE, 0L)
                + result.getOrDefault(CarStatus.MAINTENANCE, 0L))
                .build();
    }

    public CarDetailDto createCar( // 차량 등록
            CarRequestDto carRequest
    ) {
        if (carRepository.findByCarNumber(carRequest.getCarNumber()).isPresent()) {
            throw new CarAlreadyExistsException("차량 번호가 이미 존재합니다: " + carRequest.getCarNumber());
        }

        if (carRepository.findByEmulatorId(carRequest.getEmulatorId()).isPresent()) {
            throw new CarAlreadyExistsException("해당 Emulator ID가 이미 사용 중입니다: " + carRequest.getEmulatorId());
        }
        var entity = CarEntity.builder()
                .brand(carRequest.getBrand())
                .model(carRequest.getModel())
                .carYear(carRequest.getCarYear())
//                .status(CarStatus.IDLE) // default값 : 대기 중
                .status(carRequest.getStatus())
                .carType(carRequest.getCarType())
                .carNumber(carRequest.getCarNumber())
                .sumDist(carRequest.getSumDist())
                .emulatorId(carRequest.getEmulatorId())
                .build();

        return CarDetailDto.EntityToDto(carRepository.save(entity));
    }


    public CarDetailDto updateCar( // 차량 정보 업데이트
            CarRequestDto carRequest,
            String carNumber
    ) {
        CarEntity entity = carRepository.findByCarNumber(carNumber)
                    .orElseThrow(() -> new CarNotFoundException(carNumber));

        if (carRequest.getBrand() != null && !carRequest.getBrand().isBlank()) {
            entity.setBrand(carRequest.getBrand());
        }

        if(carRequest.getModel() != null && !carRequest.getModel().isBlank()) {
            entity.setModel(carRequest.getModel());
        }

        if(carRequest.getCarYear() != null && !carRequest.getCarYear().equals(0)) {
            entity.setCarYear(carRequest.getCarYear());
        }

        if(carRequest.getStatus() != null && !carRequest.getStatus().name().isBlank()) {
            var status = CarStatus.fromDisplayName(carRequest.getStatus().getDisplayName());
            entity.setStatus(status);
        }

        if(carRequest.getCarType() != null && !carRequest.getCarType().isBlank()) {
            entity.setCarType(carRequest.getCarType());
        }

        if(carRequest.getCarNumber() != null && !carRequest.getCarNumber().isBlank()) {
            entity.setCarNumber(carRequest.getCarNumber());
        }

        if (carRequest.getSumDist() >= 0) {
            entity.setSumDist(carRequest.getSumDist());
        }

        if (carRequest.getEmulatorId() != null) {
            entity.setEmulatorId(carRequest.getEmulatorId());
        }

        return CarDetailDto.EntityToDto(carRepository.save(entity));
    }


    public CarDeleteDto deleteCar( // 차량 삭제
            String carNumber
    ){
        CarEntity entity = carRepository.findByCarNumber(carNumber)
                .orElseThrow(() -> new CarNotFoundException(carNumber));

        carRepository.delete(entity);

        return CarDeleteDto.EntityToDto(entity);
    }
}
