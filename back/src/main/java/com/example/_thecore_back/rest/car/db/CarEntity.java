package com.example._thecore_back.rest.car.db;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "CAR")
public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // PK

    private String model; // 모델명

    private String brand; // 브랜드명

    @Column(name = "\"YEAR\"")
    private String year; // 연식

//    @Enumerated(EnumType.STRING)
//    private CarStatus status; // 차량 상태
    private String status;

    private String carType; // 차종

    private String carNumber; // 차량 번호

    private Float sumDist; // 총 거리

    private Integer emulatorId; // 연결된 애뮬레이터 아이디

}
