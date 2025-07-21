package com.example._thecore_back.car.controller.dto;

import com.example._thecore_back.car.domain.CarStatus;
import com.example._thecore_back.car.validation.group.CreateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarRequestDto {
    @NotBlank(groups = CreateGroup.class)
    private String brand;

    @NotBlank(groups = CreateGroup.class)
    private String model;

    private Integer carYear;

    private String status;

    @NotBlank(groups = CreateGroup.class)
    private String carType;

    @NotBlank(groups = CreateGroup.class)
    private String carNumber;

    @NotNull(groups = CreateGroup.class)
    private Double sumDist;
}
