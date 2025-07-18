package com.example._thecore_back.rest.emulator.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FinalGetEmulatorResponse {
    private String message;
    private GetEmulatorResponseData data;
}
