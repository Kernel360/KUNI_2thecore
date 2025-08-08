package com.example.mainserver.car.controller.dto;

import com.example.common.domain.car.CarEntity;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CarDetailDto {

    private String brand;

    private String model;

    private Integer carYear;

    private String status;

    private String carType;

    private String carNumber;

    private String lastLatitude;

    private String lastLongitude;

    private double sumDist;

    private String loginId;


    public static CarDetailDto EntityToDto(CarEntity car){
        return CarDetailDto.builder()
                .brand(car.getBrand())
                .model(car.getModel())
                .carYear(car.getCarYear())
                .status(car.getStatus().getDisplayName())
                .carType(car.getCarType())
                .carNumber(car.getCarNumber())
                .sumDist(car.getSumDist())
                .lastLatitude(car.getLastLatitude())
                .lastLongitude(car.getLastLongitude())
                .loginId(car.getLoginId())
                .build();

    }

}
