package com.example._thecore_back.emulator.controller;

import com.example._thecore_back.common.dto.ApiResponse;
import com.example._thecore_back.emulator.application.EmulatorService;
import com.example._thecore_back.emulator.controller.dto.*;
import com.example._thecore_back.emulator.domain.EmulatorEntity;
import com.example._thecore_back.emulator.domain.EmulatorStatus;
import com.example._thecore_back.emulator.exception.CarNotFoundException;
import com.example._thecore_back.emulator.exception.DuplicateEmulatorException;
import com.example._thecore_back.emulator.exception.EmulatorNotFoundException;

import org.hibernate.sql.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmulatorControllerTest {

    @Mock
    private EmulatorService emulatorService;

    @Mock
    private EmulatorConverter emulatorConverter;

    @InjectMocks
    private EmulatorController emulatorController;

    private EmulatorRequest emulatorRequest;
    private EmulatorEntity emulatorEntity;
    private GetEmulatorResponseData getEmulatorResponseData;

    @BeforeEach
    void setUp() {
        emulatorRequest = new EmulatorRequest();
        emulatorRequest.setCarNumber("123가 4567");

        emulatorEntity = EmulatorEntity.builder()
                .id(1L)
                .carNumber("123가 4567")
                .status(EmulatorStatus.OFF)
                .build();

        getEmulatorResponseData = GetEmulatorResponseData.builder()
                .emulatorId(1L)
                .carNumber("123가 4567")
                .emulatorStatus(EmulatorStatus.OFF)
                .build();
    }

    @Test
    @DisplayName("애뮬레이터 등록 성공")
    void registerEmulator_success() {
        when(emulatorService.registerEmulator(any(EmulatorRequest.class))).thenReturn(emulatorEntity);
        when(emulatorConverter.toRegisterEmulatorData(any(EmulatorEntity.class)))
                .thenReturn(new RegisterEmulatorResponseData(1L, "123가4567", EmulatorStatus.OFF));

        ResponseEntity<ApiResponse<RegisterEmulatorResponseData>> response = emulatorController.registerEmulator(emulatorRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("애뮬레이터가 등록되었습니다.", response.getBody().getMessage());
        verify(emulatorService).registerEmulator(any(EmulatorRequest.class));
    }

    @Test
    @DisplayName("애뮬레이터 등록 실패 - 차량 없음")
    void registerEmulator_carNotFound() {
        when(emulatorService.registerEmulator(any())).thenThrow(new CarNotFoundException("not found"));

        assertThrows(CarNotFoundException.class, () ->
                emulatorController.registerEmulator(emulatorRequest));
        verify(emulatorService).registerEmulator(any());
    }

    @Test
    @DisplayName("애뮬레이터 등록 실패 - 해당 차량에 이미 애뮬레이터 연결됨")
    void registerEmulator_carAlreadyHasEmulator() {
        when(emulatorService.registerEmulator(any())).thenThrow(new DuplicateEmulatorException("duplicate"));

        assertThrows(DuplicateEmulatorException.class, () ->
                emulatorController.registerEmulator(emulatorRequest));
        verify(emulatorService).registerEmulator(any());
    }

    @Test
    @DisplayName("애뮬레이터 상세 조회 성공")
    void getEmulator_success() {
        when(emulatorService.getEmulator(anyLong())).thenReturn(emulatorEntity);
        when(emulatorConverter.toGetEmulatorData(any(EmulatorEntity.class))).thenReturn(getEmulatorResponseData);

        ResponseEntity<ApiResponse<GetEmulatorResponseData>> response = emulatorController.getEmulator(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getData().getEmulatorId());
        verify(emulatorService).getEmulator(1L);
    }

    @Test
    @DisplayName("애뮬레이터 상세 조회 실패 - 애뮬레이터 없음")
    void getEmulator_notFound() {
        when(emulatorService.getEmulator(anyLong())).thenThrow(new EmulatorNotFoundException("Not Found"));

        assertThrows(EmulatorNotFoundException.class, () -> emulatorController.getEmulator(1L));
        verify(emulatorService).getEmulator(1L);
    }

    @Test
    @DisplayName("애뮬레이터 전체 조회 성공")
    void getAllEmulators_success() {
        List<EmulatorEntity> emulators = Collections.singletonList(emulatorEntity);
        when(emulatorService.getAllEmulators()).thenReturn(emulators);
        when(emulatorConverter.toGetEmulatorData(any(EmulatorEntity.class))).thenReturn(getEmulatorResponseData);

        ResponseEntity<ApiResponse<List<GetEmulatorResponseData>>> response = emulatorController.getAllEmulators();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
        verify(emulatorService).getAllEmulators();
    }

    @Test
    @DisplayName("애뮬레이터 전체 조회 성공 - 애뮬레이터가 없을 때 빈 리스트 반환")
    void getAllEmulators_emptyList(){
        when(emulatorService.getAllEmulators()).thenReturn(Collections.emptyList());

        ResponseEntity<ApiResponse<List<GetEmulatorResponseData>>> response = emulatorController.getAllEmulators();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getData().isEmpty());
        verify(emulatorService).getAllEmulators();
    }

    @Test
    @DisplayName("애뮬레이터 수정 성공 - 차량 번호 변경 없을 때")
    void updateEmulator_noCarNumberChange_success() {
        when(emulatorService.updateEmulator(anyLong(), any())).thenReturn(emulatorEntity);
        when(emulatorConverter.toUpdateEmulatorData(any()))
                .thenReturn(UpdateEmulatorResponseData.builder().emulatorId(1L).carNumber("123가4567").build());

        ResponseEntity<ApiResponse<UpdateEmulatorResponseData>> response = emulatorController.updateEmulator(1L, emulatorRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("애뮬레이터 정보가 수정되었습니다.", response.getBody().getMessage());
        verify(emulatorService).updateEmulator(1L, emulatorRequest);
    }

    @Test
    @DisplayName("애뮬레이터 수정 성공 - 차량 번호 변경 있을 때")
    void updateEmulator_carNumberChange_success() {
        EmulatorRequest newRequest = new EmulatorRequest();
        newRequest.setCarNumber("789다 9999");

        when(emulatorService.updateEmulator(eq(1L), any())).thenReturn(emulatorEntity);
        when(emulatorConverter.toUpdateEmulatorData(any()))
                .thenReturn(UpdateEmulatorResponseData.builder().emulatorId(1L).carNumber("789다 9999").build());

        ResponseEntity<ApiResponse<UpdateEmulatorResponseData>> response = emulatorController.updateEmulator(1L, newRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("애뮬레이터 정보가 수정되었습니다.", response.getBody().getMessage());
        verify(emulatorService).updateEmulator(1L, newRequest);
    }

    // X
    @Test
    @DisplayName("애뮬레이터 수정 성공")
    void updateEmulator_success() {
        // given
        when(emulatorService.updateEmulator(anyLong(), any(EmulatorRequest.class))).thenReturn(emulatorEntity);
        when(emulatorConverter.toUpdateEmulatorData(any(EmulatorEntity.class)))
                .thenReturn(UpdateEmulatorResponseData.builder().emulatorId(1L).carNumber("123가4567").build());

        // when
        ResponseEntity<ApiResponse<UpdateEmulatorResponseData>> response = emulatorController.updateEmulator(1L, emulatorRequest);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("애뮬레이터 정보가 수정되었습니다.", response.getBody().getMessage());
        verify(emulatorService).updateEmulator(1L, emulatorRequest);
    }

    @Test
    @DisplayName("애뮬레이터 수정 실패 - 애뮬레이터 없음")
    void updateEmulator_emulatorNotFound() {
        when(emulatorService.updateEmulator(anyLong(), any())).thenThrow(new EmulatorNotFoundException("not found"));

        assertThrows(EmulatorNotFoundException.class, () ->
                emulatorController.updateEmulator(1L, emulatorRequest));
        verify(emulatorService).updateEmulator(1L, emulatorRequest);
    }

    @Test
    @DisplayName("애뮬레이터 수정 실패 - 변경할 차량 없음")
    void updateEmulator_newCarNotFound() {
        when(emulatorService.updateEmulator(anyLong(), any())).thenThrow(new CarNotFoundException("no car"));

        assertThrows(CarNotFoundException.class, () ->
                emulatorController.updateEmulator(1L, emulatorRequest));
        verify(emulatorService).updateEmulator(1L, emulatorRequest);
    }

    @Test
    @DisplayName("애뮬레이터 삭제 성공")
    void deleteEmulator_success() {
        doNothing().when(emulatorService).deleteEmulator(anyLong());

        ResponseEntity<ApiResponse<DeleteEmulatorResponseData>> response = emulatorController.deleteEmulator(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("애뮬레이터가 삭제되었습니다.", response.getBody().getMessage());
        verify(emulatorService).deleteEmulator(1L);
    }

    @Test
    @DisplayName("애뮬레이터 삭제 실패 - 애뮬레이터 없음")
    void deleteEmulator_notFound() {
        doThrow(new EmulatorNotFoundException("not found")).when(emulatorService).deleteEmulator(1L);

        assertThrows(EmulatorNotFoundException.class, () ->
                emulatorController.deleteEmulator(1L));
        verify(emulatorService).deleteEmulator(1L);
    }

}
