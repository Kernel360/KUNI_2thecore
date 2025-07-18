package com.example._thecore_back.rest.emulator.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmulatorResponse {

    private Long id;            // 애뮬레이터 id
    private String carNumber;   // 차량 번호
    private EmulatorStatus status;      // 상태
}
