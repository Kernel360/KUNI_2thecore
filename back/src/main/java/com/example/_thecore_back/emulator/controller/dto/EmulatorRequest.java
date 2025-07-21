package com.example._thecore_back.emulator.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmulatorRequest {

    @NotNull(message = "차량 번호는 필수입니다.")
    // TODO: car_number -> device_id 임시 변경. 추후 car테이블 머지 후 수정 필요
    private String deviceId;
}
