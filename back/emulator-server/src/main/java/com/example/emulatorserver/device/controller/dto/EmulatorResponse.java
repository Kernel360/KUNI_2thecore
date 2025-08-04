package com.example.emulatorserver.device.controller.dto;

import com.example.common.domain.emulator.EmulatorStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmulatorResponse {
    private String deviceId;            // 애뮬레이터 id
    private String carNumber;   // 차량 번호
    private EmulatorStatus status;      // 상태
}
