package com.example.mainserver.car.controller.dto;


import com.example.common.domain.car.CarEntity;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
//@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CarSearchDto {

    private String carNumber;

    private String brand;

    private String model;

    private String status;

    public static CarSearchDto EntityToDto(CarEntity car){
        return CarSearchDto.builder()
                .carNumber(car.getCarNumber())
                .brand(car.getBrand())
                .model(car.getModel())
                .status(car.getStatus().getDisplayName())
                .build();

    }




}
