package com.example._thecore_back.emulator.controller;

import com.example._thecore_back.emulator.controller.dto.*;
import com.example._thecore_back.emulator.domain.EmulatorEntity;
import com.example._thecore_back.emulator.application.EmulatorService;
import com.example._thecore_back.common.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/emulators")
@RequiredArgsConstructor
public class EmulatorController {

    private final EmulatorService emulatorService;
    private final EmulatorConverter emulatorConverter;

    // 애뮬레이터 등록
    @PostMapping
    public ResponseEntity<ApiResponse<RegisterEmulatorResponseData>> registerEmulator(
        @Valid @RequestBody EmulatorRequest emulatorRequest
    ){
        EmulatorEntity savedEntity = emulatorService.registerEmulator(emulatorRequest);
        RegisterEmulatorResponseData responseData = emulatorConverter.toRegisterEmulatorData(savedEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("애뮬레이터가 등록되었습니다.", responseData));
    }


    // 애뮬레이터 상세 조회
    @GetMapping("/{emulator_id}")
    public ResponseEntity<ApiResponse<GetEmulatorResponseData>> getEmulator(
            @PathVariable("emulator_id") Long emulatorId
    ) {
        EmulatorEntity getEntity = emulatorService.getEmulator(emulatorId);
        GetEmulatorResponseData responseData = emulatorConverter.toGetEmulatorData(getEntity);

        return ResponseEntity.ok(ApiResponse.success(null, responseData));
    }

    // 애뮬레이터 전체 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<GetEmulatorResponseData>>> getAllEmulators() {
        List<EmulatorEntity>  getEntities = emulatorService.getAllEmulators();
        List<GetEmulatorResponseData> responseDataList = getEntities.stream()
                .map(emulatorConverter::toGetEmulatorData)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(null, responseDataList));
    }

    // 애뮬레이터 수정
    @PatchMapping("/{emulator_id}")
    public ResponseEntity<ApiResponse<UpdateEmulatorResponseData>> updateEmulator(
            @PathVariable("emulator_id") Long emulatorId,
            @Valid @RequestBody EmulatorRequest emulatorRequest
    ){
        EmulatorEntity updatedEntity = emulatorService.updateEmulator(emulatorId, emulatorRequest);
        UpdateEmulatorResponseData responseData = emulatorConverter.toUpdateEmulatorData(updatedEntity);

        return ResponseEntity.ok(ApiResponse.success("애뮬레이터 정보가 수정되었습니다.", responseData));
    }

    // 애뮬레이터 삭제
    @DeleteMapping("/{emulator_id}")
    public ResponseEntity<ApiResponse<DeleteEmulatorResponseData>> deleteEmulator(
            @PathVariable("emulator_id") Long emulatorId
    ){
        emulatorService.deleteEmulator(emulatorId);
        DeleteEmulatorResponseData responseData = DeleteEmulatorResponseData.builder()
                .emulatorId(emulatorId)
                .build();

        return ResponseEntity.ok(ApiResponse.success("애뮬레이터가 삭제되었습니다.", responseData));
    }
}
