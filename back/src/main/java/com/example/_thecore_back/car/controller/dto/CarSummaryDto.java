package com.example._thecore_back.car.controller.dto;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarSummaryDto {


    private Long total;
    private Long operating;
    private Long waiting;
    private Long inspecting;



}
