package com.example._thecore_back.emulator.controller.dto;

import com.example._thecore_back.emulator.domain.EmulatorStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RegisterEmulatorResponseData {
    private Long emulatorId;
    private String carNumber;
    private EmulatorStatus emulatorStatus;
}
