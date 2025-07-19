package com.example._thecore_back.emulator.infrastructure;

import com.example._thecore_back.emulator.domain.EmulatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmulatorRepository extends JpaRepository<EmulatorEntity, Long> {
    // TODO: car_number -> device_id 임시 변경. 추후 car테이블 머지 후 수정 필요
    Optional<EmulatorEntity> findByDeviceId(String deviceId);
}
