package com.example._thecore_back.emulator.controller.dto;

import com.example._thecore_back.emulator.domain.EmulatorEntity;
import org.springframework.stereotype.Component;

@Component
public class EmulatorConverter {

    public EmulatorResponse toResponse(EmulatorEntity emulatorEntity) {
        EmulatorResponse response = new EmulatorResponse();
        response.setId(emulatorEntity.getId());
        response.setCarNumber(emulatorEntity.getCarNumber());
        response.setStatus(emulatorEntity.getStatus());
        return response;
    }

    public EmulatorEntity toEntity(EmulatorResponse emulatorResponse) {
        EmulatorEntity entity = new EmulatorEntity();
        entity.setId(emulatorResponse.getId());
        entity.setCarNumber(emulatorResponse.getCarNumber());
        entity.setStatus(emulatorResponse.getStatus());
        return entity;
    }

    // 애뮬레이터 등록
    public RegisterEmulatorResponseData toRegisterEmulatorData(
            EmulatorEntity emulatorEntity
    ) {
        return RegisterEmulatorResponseData.builder()
                .emulatorId(emulatorEntity.getId())
                .carNumber(emulatorEntity.getCarNumber())
                .emulatorStatus(emulatorEntity.getStatus())
                .build()
                ;
    }

    // 애뮬레이터 상세 조회
    public GetEmulatorResponseData toGetEmulatorData(
            EmulatorEntity emulatorEntity
    ) {
        return GetEmulatorResponseData.builder()
                .emulatorId(emulatorEntity.getId())
                .carNumber(emulatorEntity.getCarNumber())
                .emulatorStatus(emulatorEntity.getStatus())
                .build()
                ;
    }

    // 애뮬레이터 수정
    public UpdateEmulatorResponseData toUpdateEmulatorData(
            EmulatorEntity emulatorEntity
    ) {
        return UpdateEmulatorResponseData.builder()
                .emulatorId(emulatorEntity.getId())
                .carNumber(emulatorEntity.getCarNumber())
                .build()
                ;
    }
}
