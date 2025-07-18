package com.example._thecore_back.rest.emulator.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmulatorRequest {

    @NotNull(message = "차량 번호는 필수입니다.")
    private String carNumber;
}
