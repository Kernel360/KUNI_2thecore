package com.example._thecore_back.rest.emulator.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FinalGetAllEmulatorResponse {
    private String message;
    private List<GetEmulatorResponseData> data;
}
