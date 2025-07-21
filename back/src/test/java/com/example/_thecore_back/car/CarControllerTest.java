package com.example._thecore_back.car;

import com.example._thecore_back.car.application.CarService;
import com.example._thecore_back.car.controller.CarController;
import com.example._thecore_back.car.controller.dto.*;
import com.example._thecore_back.car.domain.CarStatus;
import com.example._thecore_back.car.exception.CarAlreadyExistsException;
import com.example._thecore_back.car.exception.CarErrorCode;
import com.example._thecore_back.car.exception.CarNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
public class CarControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST - 차량 등록 컨트롤러 Test")
    void createCarSuccess() throws Exception {
        // 요청 객체 생성
        CarRequestDto request = CarRequestDto.builder()
                .brand("현대")
                .model("아반떼")
                .carYear(2025)
                .carType("중형")
                .carNumber("12가3456")
                .sumDist(1234.56)
                .build();

        // 응답 객체 생성
        CarDetailDto response = CarDetailDto.builder()
                .brand("현대")
                .model("아반떼")
                .carYear(2025)
                .status("대기")
                .carType("중형")
                .carNumber("12가3456")
                .sumDist(1234.56)
                .build();

        // 기대 return 형식 지정
        when(carService.createCar(any(CarRequestDto.class)))
                .thenReturn(response);

        // test 실행
        ResultActions actions = mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        // 결과 검증
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.brand").value("현대"))
                .andExpect(jsonPath("$.data.model").value("아반떼"))
                .andExpect(jsonPath("$.data.car_year").value(2025))
                .andExpect(jsonPath("$.data.status").value("대기"))
                .andExpect(jsonPath("$.data.car_type").value("중형"))
                .andExpect(jsonPath("$.data.car_number").value("12가3456"))
                .andExpect(jsonPath("$.data.sum_dist").value(1234.56));
    }

    @Test
    @DisplayName("PATCH - 차량 정보 수정 컨트롤러 Test")
    void updateCarSuccess() throws Exception {
        CarRequestDto request = CarRequestDto.builder()
                .carYear(2015)
                .status("운행")
                .carNumber("6543나21")
                .build();

        CarDetailDto response = CarDetailDto.builder()
                .brand("현대")
                .model("아반떼")
                .carYear(2015)
                .status("운행")
                .carType("중형")
                .carNumber("6543나21")
                .sumDist(1234.56)
                .build();

        when(carService.updateCar(any(CarRequestDto.class), any(String.class)))
                .thenReturn(response);

        ResultActions actions = mockMvc.perform(patch("/api/cars/1234가56")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.car_year").value(2015))
                .andExpect(jsonPath("$.data.status").value("운행"))
                .andExpect(jsonPath("$.data.car_number").value("6543나21"));
    }

    @Test
    @DisplayName("DELETE - 차량 삭제 컨트롤러 Test")
    void deleteCarSuccess() throws Exception {
        CarDeleteDto response = CarDeleteDto.builder()
                .brand("현대")
                .model("아반떼")
                .carNumber("6543나21")
                .build();

        when(carService.deleteCar(any(String.class)))
                .thenReturn(response);

        ResultActions actions = mockMvc.perform(delete("/api/cars/6543나21"));

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.brand").value("현대"))
                .andExpect(jsonPath("$.data.model").value("아반떼"))
                .andExpect(jsonPath("$.data.car_number").value("6543나21"));
    }

    // 예외처리 Test Code
    @Test
    @DisplayName("POST - 차량 등록 실패: 차량 번호 중복")
    void createCarFailCarNum() throws Exception {
        CarRequestDto request = CarRequestDto.builder()
                .brand("현대")
                .model("아반떼")
                .carYear(2025)
                .carType("중형")
                .carNumber("12가3456")
                .sumDist(1234.56)
                .build();

        when(carService.createCar(any(CarRequestDto.class)))
                .thenThrow(new CarAlreadyExistsException("12가3456"));

        ResultActions actions = mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        actions
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.data.status").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.data.error").value(HttpStatus.CONFLICT.getReasonPhrase()))
                .andExpect(jsonPath("$.data.message").value("이미 등록된 차량입니다: 12가3456"))
                .andExpect(jsonPath("$.data.path").value("/api/cars"));
    }

    @Test
    @DisplayName("PATCH - 차량 정보 수정 실패: 존재하지 않는 차량")
    void updateCarFailNotFound() throws Exception {
        CarRequestDto request = CarRequestDto.builder()
                .carYear(2015)
                .status("운행")
                .carNumber("6543나21")
                .build();

        when(carService.updateCar(any(CarRequestDto.class), any(String.class)))
                .thenThrow(new CarNotFoundException(CarErrorCode.CAR_NOT_FOUND_BY_NUMBER, "1234가56"));

        ResultActions actions = mockMvc.perform(patch("/api/cars/1234가56")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.data.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.data.error").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.data.message").value("해당 차량 ( 1234가56 )은 존재하지 않습니다. 다시 입력해주세요"));
    }

    @Test
    @DisplayName("PATCH - 차량 정보 수정 실패: 이미 존재하는 차량 번호")
    void updateCarFailAlreadyExists() throws Exception {
        CarRequestDto request = CarRequestDto.builder()
                .carYear(2015)
                .status("운행")
                .carNumber("6543나21")
                .build();

        when(carService.updateCar(any(CarRequestDto.class), any(String.class)))
                .thenThrow(new CarAlreadyExistsException("6543나21"));

        ResultActions actions = mockMvc.perform(patch("/api/cars/1234가56")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        actions
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.data.status").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.data.error").value(HttpStatus.CONFLICT.getReasonPhrase()))
                .andExpect(jsonPath("$.data.message").value("이미 등록된 차량입니다: 6543나21"));
    }

    @Test
    @DisplayName("DELETE - 차량 삭제 실패: 존재하지 않는 차량")
    void deleteCarFail() throws Exception {
        when(carService.deleteCar("9999가99"))
                .thenThrow(new CarNotFoundException(CarErrorCode.CAR_NOT_FOUND_BY_NUMBER, "9999가99"));

        ResultActions actions = mockMvc.perform(delete("/api/cars/9999가99"));

        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.data.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.data.error").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.data.message").value("해당 차량 ( 9999가99 )은 존재하지 않습니다. 다시 입력해주세요"));
    }

    @Test
    @DisplayName("차량 번호를 통해 해당 차량을 조회한다.")
    public void getCarByCarNumber() throws Exception {
        var response = CarDetailDto.builder()
                .brand("현대")
                .model("아이오닉")
                .carNumber("12가1234")
                .build();

        when(carService.getCar("12가1234")).thenReturn(response);

        mockMvc.perform(get("/api/cars/12가1234"))
                .andDo(print())
                .andExpect(jsonPath("$.data.car_number").value("12가1234"))
                .andExpect(jsonPath("$.data.model").value("아이오닉"));


        when(carService.getCar("1234")).thenThrow(new CarNotFoundException(CarErrorCode.CAR_NOT_FOUND_BY_NUMBER,"1234"));

        mockMvc.perform(get("/api/cars/1234"))
                .andDo(print())
                .andExpect(jsonPath("$.data.status").value(404))
                .andExpect(jsonPath("$.data.message").value("해당 차량 ( 1234 )은 존재하지 않습니다. 다시 입력해주세요"));
    }

    @Test
    @DisplayName("전체 차량 조회")
    public void getAllCars() throws Exception {

        List<CarSearchDto> carList = List.of(new CarSearchDto("12가1234", "현대", "아이오닉", "IN_IDLE")
                ,new CarSearchDto("12가3423", "기아", "아이오닉", "IN_IDLE"));

        when(carService.getAllCars()).thenReturn(carList);

        mockMvc.perform(get("/api/cars"))
                .andDo(print())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].car_number").value("12가1234"))
                .andExpect(jsonPath("$.data[1].car_number").value("12가3423"));

    }

    @Test
    @DisplayName("전체 차량 조회 - 등록된 차량이 존재하지 않은 경우")
    public void getAllCarsFailed() throws Exception {
        // 차량이 존재하지 않을때
        when(carService.getAllCars()).thenThrow(new CarNotFoundException(CarErrorCode.NO_REGISTERED_CAR));

        mockMvc.perform(get("/api/cars"))
                .andDo(print())
                .andExpect(jsonPath("$.data.status").value(404))
                .andExpect(jsonPath("$.data.message").value("등록된 차량이 존재하지 않습니다."));

    }

    @Test
    @DisplayName("차량 상태에 따른 대시보드 테스트 출력")
    public void getCarByStatus() throws Exception {

        List<CarSearchDto> carList = List.of(new CarSearchDto("12가1234", "현대", "아이오닉", "IN_IDLE")
                ,new CarSearchDto("12가3423", "기아", "아이오닉", "IN_IDLE"),
                new CarSearchDto("12가2315", "기아", "그렌저", "MAINTENANCE"));

        var response = CarSummaryDto.builder()
                .total(3L)
                .operating(0L)
                .waiting(2L)
                .inspecting(1L)
                .build();

        when(carService.getCountByStatus()).thenReturn(response);

        mockMvc.perform(get("/api/cars/statistics"))
                .andDo(print())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.data.total").value(3L))
                .andExpect(jsonPath("$.data.waiting").value(2L))
                .andExpect(jsonPath("$.data.inspecting").value(1L))
                .andExpect(jsonPath("$.data.operating").value(0L));
    }


    @Test
    @DisplayName("필터링 조건에 기반하여 차량 조회")
    public void getAllCarsByFilter() throws Exception {

        List<CarSearchDto> carList = List.of(new CarSearchDto("12가1234", "현대", "아이오닉", "IDLE")
                ,new CarSearchDto("12가3423", "기아", "아이오닉", "IDLE"),
                new CarSearchDto("12가2315", "기아", "그렌저", "MAINTENANCE"));

        // 전체 조회
        when(carService.getCarsByFilter(null, null, null, null)).thenReturn(carList);

        mockMvc.perform(get("/api/cars/search"))
                .andDo(print())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.data.length()").value(3));

        // 브랜드만 입력
        List<CarSearchDto> kiaCars = List.of(new CarSearchDto("12가3423", "기아", "아이오닉", "IDLE"),
                new CarSearchDto("12가2315", "기아", "그렌저", "MAINTENANCE"));

        when(carService.getCarsByFilter(null, null, "기아", null)).thenReturn(kiaCars);

        mockMvc.perform(get("/api/cars/search").param("brand", "기아"))
                .andDo(print())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].brand").value("기아"))
                .andExpect(jsonPath("$.data[1].brand").value("기아"));

        // 차량 번호만 입력
        List<CarSearchDto> carByNumber = List.of(new CarSearchDto("12가3423", "기아", "아이오닉", "IDLE"));

        when(carService.getCarsByFilter("12가3423", null, null, null)).thenReturn(carByNumber);

        mockMvc.perform(get("/api/cars/search").param("carNumber", "12가3423"))
                .andDo(print())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].brand").value("기아"))
                .andExpect(jsonPath("$.data[0].car_number").value("12가3423"));

        // 차량 상태만 입력
        List<CarSearchDto> carByStatusInIdle = List.of(new CarSearchDto("12가3423", "기아", "아이오닉", "IDLE")
                , new CarSearchDto("12가3423", "기아", "아이오닉", "IDLE"));

        when(carService.getCarsByFilter(null, null, null, CarStatus.IDLE)).thenReturn(carByStatusInIdle);

        mockMvc.perform(get("/api/cars/search").param("status", CarStatus.IDLE.name()))
                .andDo(print())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].status").value("IDLE"))
                .andExpect(jsonPath("$.data[1].status").value("IDLE"));

        // brand, model명 필터링
        List<CarSearchDto> carByBrandAndModel = List.of(new CarSearchDto("12가3423", "기아", "아이오닉", "IDLE"));

        when(carService.getCarsByFilter(null, "아이오닉", "기아", null)).thenReturn(carByBrandAndModel);

        mockMvc.perform(get("/api/cars/search").param("brand", "기아")
                        .param("model", "아이오닉"))
                .andDo(print())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].brand").value("기아"))
                .andExpect(jsonPath("$.data[0].model").value("아이오닉"));

    }

    @Test
    @DisplayName("해당 조건을 충족하는 차량이 없는경우")
    public void getCarsByFilterFailed() throws Exception {
        when(carService.getCarsByFilter(null, "삼성", null, null)).thenReturn(Collections.emptyList()
        );

        mockMvc.perform(get("/api/cars/search").param("model", "삼성"))
                .andDo(print())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.data.length()").value(0));
    }
}
