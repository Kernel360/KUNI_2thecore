package com.example._thecore_back.rest.emulator.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetEmulatorResponseData {
    private Long emulatorId;
    private String carNumber;
    private EmulatorStatus emulatorStatus;
}
