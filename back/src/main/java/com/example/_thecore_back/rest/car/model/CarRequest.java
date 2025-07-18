package com.example._thecore_back.rest.car.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarRequest {
    @NotBlank(groups = CreateGroup.class)
    private String brand;

    @NotBlank(groups = CreateGroup.class)
    private String model;

    @NotBlank(groups = CreateGroup.class)
    private String year;

    private String status;

    @NotBlank(groups = CreateGroup.class)
    private String carType;

    @NotBlank(groups = CreateGroup.class)
    private String carNumber;

    @NotNull(groups = CreateGroup.class)
    private Float sumDist;

    @NotNull(groups = CreateGroup.class)
    private Integer emulatorId;

    @NotBlank
    private String verificationCode;

    @NotBlank
    private String confirmPassword;
}
