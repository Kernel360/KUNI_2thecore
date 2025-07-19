package com.example._thecore_back.car.exception;


import com.example._thecore_back.car.controller.CarController;
import com.example._thecore_back.car.exception.response.CarExceptionResponse;
import com.example._thecore_back.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice(basePackageClasses =  CarController.class)
@Order(1)

public class CarExceptionHandler {

    // 차량이 이미 존재할 때 - create
    @ExceptionHandler(CarAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<CarExceptionResponse>> handleCarAlreadyExistsException(CarAlreadyExistsException e, HttpServletRequest request) {
        var response = CarExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT.value())
                .body(ApiResponse.fail(response));
    }

    // 차량이 존재하지 않을 때
    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<ApiResponse<CarExceptionResponse>> handleCarNotFoundException(CarNotFoundException e, HttpServletRequest request) {

        var response = CarExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND.value())
                .body(ApiResponse.fail(response));
    }
}
