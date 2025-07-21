package com.example._thecore_back.car.controller.dto;

import com.example._thecore_back.car.domain.CarEntity;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)

public class CarDetailDto {

    private String brand;

    private String model;

    private Integer carYear;

    private String status;

    private String carType;

    private String carNumber;

    private double sumDist;


    public static CarDetailDto EntityToDto(CarEntity car){
        return CarDetailDto.builder()
                .brand(car.getBrand())
                .model(car.getModel())
                .carYear(car.getCarYear())
                .status(car.getStatus().getDisplayName())
                .carType(car.getCarType())
                .carNumber(car.getCarNumber())
                .sumDist(car.getSumDist())
                .build();

    }

}
