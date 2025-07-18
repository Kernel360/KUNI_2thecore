package com.example._thecore_back.rest.car.model;

import com.example._thecore_back.rest.car.db.CarEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarResponse {
    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotBlank
    private String year;

    @NotBlank
    private String status;

    @NotBlank
    private String carType;

    @NotBlank
    private String carNumber;

    @NotNull
    private Float sumDist;

    @NotNull
    private Integer emulatorId;

    public static CarResponse from(CarEntity carEntity) {
        return CarResponse.builder()
                .brand(carEntity.getBrand())
                .model(carEntity.getModel())
                .year(carEntity.getYear())
//                .status(carEntity.getStatus().getDisplayName()) // enum -> 한글 문자열
                .status(carEntity.getStatus())
                .carType(carEntity.getCarType())
                .carNumber(carEntity.getCarNumber())
                .sumDist(carEntity.getSumDist())
                .emulatorId(carEntity.getEmulatorId())
                .build();
    }
}
