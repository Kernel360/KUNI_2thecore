package com.example._thecore_back.car.controller;
import com.example._thecore_back.car.controller.dto.*;
import com.example._thecore_back.car.domain.CarStatus;
import com.example._thecore_back.common.response.ApiResponse;
import com.example._thecore_back.car.validation.group.CreateGroup;
import com.example._thecore_back.car.application.CarService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

//    @GetMapping("")
//    public
    @GetMapping("/{car_number}")
    public ApiResponse<CarDetailDto> getCar(@PathVariable String car_number) {

        var response = carService.getCar(car_number);

//        return CarResponse.<CarDetailDto>builder()
//                .result("OK")
//                .message("find car")
//                .data(response)
//                .build();

        return ApiResponse.success(response);
    }

    @GetMapping
    public ApiResponse<List<CarSearchDto>> getAllCars() {

        var response = carService.getAllCars();

        return ApiResponse.success(response);
    }

    @GetMapping("/statistics")
    public ApiResponse<CarSummaryDto> getCountByStatus() {
        var response = carService.getCountByStatus();

        return ApiResponse.success(response);
    }

    /**
     * 조건에 맞는 차량들을 조회하는 메소드
     * @param carNumber : 차량 번호
     * @param model : 차량 모델명
     * @param brand : 차량 브랜드
     * @param status : 현재 차량 상태
     * @return 각 조건에 부합하는 차량 조회
     */
    @GetMapping("/search")
    public ApiResponse<List<CarSearchDto>> getCarsByFilter(
            @RequestParam(required = false) String carNumber,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) CarStatus status
    ) {
        var response = carService.getCarsByFilter(carNumber, model, brand, status);
        return ApiResponse.success(response);
    }

    // 차량 등록
    @PostMapping
    public ApiResponse<CarDetailDto> createCar(
            @RequestBody
            @Validated(CreateGroup.class)
            CarRequestDto carRequest
    ){
       var response = carService.createCar(carRequest);

        return ApiResponse.success(response, "차량 등록이 성공적으로 완료되었습니다.");
    }

    // 차량 정보 업데이트
    @PatchMapping("/{car_number}")
    public ApiResponse<CarDetailDto> updateCar(
            @PathVariable("car_number")
            String carNumber,
            @RequestBody
            @Validated
            CarRequestDto carRequest
    ){
        var response = carService.updateCar(carRequest, carNumber);

        return ApiResponse.success(response, "차량 정보가 성공적으로 수정되었습니다.");
    }

    // 차량 삭제
    @DeleteMapping("/{car_number}")
    public ApiResponse<CarDeleteDto> deleteCar(
            @PathVariable("car_number")
            String carNumber
    ){
        var response = carService.deleteCar(carNumber);

        return ApiResponse.success(response, "차량 삭제가 성공적으로 완료되었습니다.");
    }
}
