package com.example._thecore_back.rest.emulator.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FinalDeleteEmulatorResponse {
    private String message;
    private DeleteEmulatorResponseData data;
}
