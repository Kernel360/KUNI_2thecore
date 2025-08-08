package com.example.common.domain.car;

import com.example.common.dto.CarRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "Car")
@Table(name = "car")

public class CarEntity {
    @Id
    @Column(name = "car_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // PK


    private String model; // 모델명


    private String brand; // 브랜드명


    @Column(name = "car_year")
    private Integer carYear; // 연식

    @Enumerated(EnumType.STRING)
    @Column(length = 40)
    private CarStatus status; // 차량 상태


    @Column(name = "car_type")
    private String carType; // 차종

    @Column(name = "car_number")
    private String carNumber; // 차량 번호

    @Column(name = "sum_dist")
    private double sumDist; // 총 거리

    @Column(name = "last_latitude")
    private String lastLatitude;

    @Column(name = "last_longitude")
    private String lastLongitude;

    @Column(name = "login_id")
    private String loginId;

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
