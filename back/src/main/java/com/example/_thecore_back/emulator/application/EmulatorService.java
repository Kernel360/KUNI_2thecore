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

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmulatorService {

    private final EmulatorRepository emulatorRepository;
    private final CarRepository carRepository;

    // 애뮬레이터 등록
    public EmulatorEntity registerEmulator(EmulatorRequest emulatorRequest) {
        // 예외처리 1. 차량 존재 여부 확인
        // TODO: car_number -> device_id 임시 변경. 추후 car테이블 머지 후 수정 필요
        CarEntity carEntity = carRepository.findByCarNumber(emulatorRequest.getDeviceId())
                .orElseThrow(() -> new CarNotFoundException("해당하는 차량이 존재하지 않습니다: " + emulatorRequest.getDeviceId()));

        // 예외처리 2. 차량에 이미 애뮬레이터가 연결되어 있는지 확인
        if (carEntity.getEmulatorId() != null) {
            throw new DuplicateEmulatorException("해당 차량에 이미 애뮬레이터가 연결되어 있습니다: " + emulatorRequest.getDeviceId());
        }

        // 예외처리 3. 애뮬레이터 중복 등록 방지 (carNumber 기준)
        emulatorRepository.findByDeviceId(emulatorRequest.getDeviceId()).ifPresent(emulator -> {
            throw new DuplicateEmulatorException("이미 등록된 애뮬레이터입니다: " + emulatorRequest.getDeviceId());
        });

        EmulatorEntity entity = EmulatorEntity.builder()
                .carNumber(emulatorRequest.getDeviceId())  // 차량 번호
                .status(EmulatorStatus.OFF)  // 상태 설정 (초기값: OFF)
                .build();

        EmulatorEntity savedEntity = emulatorRepository.save(entity);

        carEntity.setEmulatorId(savedEntity.getId().intValue());
        carRepository.save(carEntity);

        return savedEntity;
    }

    // 애뮬레이터 상세 조회
    public EmulatorEntity getEmulator(Long id) {
        // 예외처리 1. 애뮬레이터 존재 여부 확인
        EmulatorEntity entity;
        entity = emulatorRepository.findById(id)
                .orElseThrow(() -> new EmulatorNotFoundException("해당하는 애뮬레이터가 없습니다: " + id));

        return entity;
    }

    // 애뮬레이터 전체 조회
    public List<EmulatorEntity> getAllEmulators() {

        return emulatorRepository.findAll();
    }

    // 애뮬레이터 수정
    // TODO: car_number -> device_id 임시 변경. 추후 car테이블 머지 후 수정 필요
    public EmulatorEntity updateEmulator(Long id, EmulatorRequest emulatorRequest) {
        // 예외처리 1. 애뮬레이터 존재 여부 확인
        EmulatorEntity entity = emulatorRepository.findById(id)
                .orElseThrow(() -> new EmulatorNotFoundException("해당하는 애뮬레이터가 없습니다.: " + id));

        // 차량 번호가 변경되는 경우에만 예외처리 및 애뮬레이터 업데이트 수행
        if (!entity.getDeviceId().equals(emulatorRequest.getDeviceId())) {
            // 예외처리 2. 차량 존재 여부 확인
            CarEntity newCarEntity = carRepository.findByCarNumber(emulatorRequest.getDeviceId())
                    .orElseThrow(() -> new CarNotFoundException("해당하는 차량이 존재하지 않습니다: " + emulatorRequest.getDeviceId()));

            // 예외처리 3. 차량에 이미 애뮬레이터가 연결되어 있는지 확인
            if (newCarEntity.getEmulatorId() != null) {
                throw new DuplicateEmulatorException("해당 차량에 이미 애뮬레이터가 연결되어 있습니다: " + emulatorRequest.getDeviceId());
            }

            // 예외처리 4. carNumber에 애뮬레이터가 이미 존재하는지 확인 (중복 등록 방지)
            emulatorRepository.findByDeviceId(emulatorRequest.getDeviceId()).ifPresent(emulator -> {
                throw new DuplicateEmulatorException("이미 등록된 애뮬레이터입니다: " + emulatorRequest.getDeviceId());
            });

            // 이전 차량의 emulatorId 초기화
            carRepository.findByCarNumber(entity.getDeviceId()).ifPresent(oldCar -> {
                oldCar.setEmulatorId(null);
                carRepository.save(oldCar);
            });

            // 새로운 차량의 emulatorId 업데이트
            newCarEntity.setEmulatorId(entity.getId().intValue());
            carRepository.save(newCarEntity);
        }

        entity.setCarNumber(emulatorRequest.getDeviceId());

        return emulatorRepository.save(entity);
    }

    // 애뮬레이터 삭제
    public void deleteEmulator(Long id) {
        // 예외처리 1. 애뮬레이터 존재 여부 확인
        EmulatorEntity emulatorEntity = emulatorRepository.findById(id)
                .orElseThrow(() -> new EmulatorNotFoundException("해당하는 애뮬레이터가 없습니다: " + id));

        // 연결된 차량의 emulatorId 초기화
        carRepository.findByCarNumber(emulatorEntity.getDeviceId()).ifPresent(carEntity -> {
            carEntity.setEmulatorId(null);
            carRepository.save(carEntity);
        });

        emulatorRepository.deleteById(id);  // 애뮬레이터 삭제
    }
}

