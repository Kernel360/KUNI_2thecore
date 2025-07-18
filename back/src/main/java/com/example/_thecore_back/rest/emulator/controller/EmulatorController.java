package com.example._thecore_back.rest.emulator.controller;

import com.example._thecore_back.rest.emulator.db.EmulatorEntity;
import com.example._thecore_back.rest.emulator.model.*;
import com.example._thecore_back.rest.emulator.service.EmulatorService;

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
    public ResponseEntity<FinalRegisterEmulatorResponse> registerEmulator(
        @Valid @RequestBody EmulatorRequest emulatorRequest
    ){
        EmulatorEntity savedEntity = emulatorService.registerEmulator(emulatorRequest);
        RegisterEmulatorResponseData responseData = emulatorConverter.toRegisterEmulatorData(savedEntity);
        FinalRegisterEmulatorResponse finalResponse = FinalRegisterEmulatorResponse.builder()
                .message("애뮬레이터가 등록되었습니다.")
                .data(responseData)
                .build()
                ;

        return ResponseEntity.status(HttpStatus.CREATED).body(finalResponse);
    }


    // 애뮬레이터 상세 조회
    @GetMapping("/{emulator_id}")
    public ResponseEntity<FinalGetEmulatorResponse> getEmulator(
            @PathVariable("emulator_id") Long emulatorId
    ) {
        EmulatorEntity getEntity = emulatorService.getEmulator(emulatorId);
        GetEmulatorResponseData responseData = emulatorConverter.toGetEmulatorData(getEntity);
        FinalGetEmulatorResponse finalResponse = FinalGetEmulatorResponse.builder()
                .message(null)
                .data(responseData)
                .build()
                ;

        return ResponseEntity.ok(finalResponse);
    }

    // 애뮬레이터 전체 조회
    @GetMapping
    public ResponseEntity<FinalGetAllEmulatorResponse> getAllEmulators() {
        List<EmulatorEntity>  getEntities = emulatorService.getAllEmulators();
        List<GetEmulatorResponseData> responseDataList = getEntities.stream()
                .map(emulatorConverter::toGetEmulatorData)
                .collect(Collectors.toList());
        FinalGetAllEmulatorResponse finalResponse = FinalGetAllEmulatorResponse.builder()
                .message(null)
                .data(responseDataList)
                .build()
                ;

        return ResponseEntity.ok(finalResponse);
    }

    // 애뮬레이터 수정
    @PatchMapping("/{emulator_id}")
    public ResponseEntity<FinalUpdateEmulatorResponse> updateEmulator(
            @PathVariable("emulator_id") Long emulatorId,
            @Valid @RequestBody EmulatorRequest emulatorRequest
    ){
        EmulatorEntity updatedEntity = emulatorService.updateEmulator(emulatorId, emulatorRequest);
        UpdateEmulatorResponseData responseData = emulatorConverter.toUpdateEmulatorData(updatedEntity);
        FinalUpdateEmulatorResponse finalResponse = FinalUpdateEmulatorResponse.builder()
                .message("애뮬레이터 정보가 수정되었습니다.")
                .data(responseData)
                .build()
                ;

        return ResponseEntity.ok(finalResponse);
    }

    // 애뮬레이터 삭제
    @DeleteMapping("/{emulator_id}")
    public ResponseEntity<FinalDeleteEmulatorResponse> deleteEmulator(
            @PathVariable("emulator_id") Long emulatorId
    ){
        emulatorService.deleteEmulator(emulatorId);
        DeleteEmulatorResponseData responseData = DeleteEmulatorResponseData.builder()
                .emulatorId(emulatorId)
                .build();
        FinalDeleteEmulatorResponse finalResponse = FinalDeleteEmulatorResponse.builder()
                .message("애뮬레이터가 삭제되었습니다.")
                .data(responseData)
                .build()
                ;

        return ResponseEntity.ok(finalResponse);
    }
}
