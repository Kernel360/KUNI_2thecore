package com.example._thecore_back.car.domain;

import com.example._thecore_back.car.controller.dto.CarDetailDto;
import com.example._thecore_back.car.controller.dto.CarRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "Car")

public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // PK


    private String model; // 모델명


    private String brand; // 브랜드명


    @Column(name = "car_year")
    private Integer carYear; // 연식

    @Enumerated(EnumType.STRING)
    @Column(length = 40)
    private CarStatus status; // 차량 상태


    private String carType; // 차종

    private String carNumber; // 차량 번호

    private double sumDist; // 총 거리

    private Integer emulatorId; // 연결된 애뮬레이터 아이디

    public void updateInfo(CarRequestDto carRequest) {
        if (carRequest.getBrand() != null && !carRequest.getBrand().isBlank()) {
            setBrand(carRequest.getBrand());
        }

        if(carRequest.getModel() != null && !carRequest.getModel().isBlank()) {
            setModel(carRequest.getModel());
        }

        if(carRequest.getCarYear() != null && !carRequest.getCarYear().equals(0)) {
            setCarYear(carRequest.getCarYear());
        }

        if(carRequest.getStatus() != null && !carRequest.getStatus().isBlank()) {
            var status = CarStatus.fromDisplayName(carRequest.getStatus());
            setStatus(status);
        }

        if(carRequest.getCarType() != null && !carRequest.getCarType().isBlank()) {
            setCarType(carRequest.getCarType());
        }

        if(carRequest.getCarNumber() != null && !carRequest.getCarNumber().isBlank()) {
            setCarNumber(carRequest.getCarNumber());
        }

        if (carRequest.getSumDist() != null && carRequest.getSumDist() >= 0) {
            setSumDist(carRequest.getSumDist());
        }
    }
}
