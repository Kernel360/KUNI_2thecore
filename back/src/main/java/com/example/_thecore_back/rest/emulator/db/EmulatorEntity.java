package com.example._thecore_back.rest.emulator.db;

import com.example._thecore_back.rest.emulator.model.EmulatorStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class EmulatorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String carNumber;   // 차량 번호

    @Enumerated(EnumType.STRING)
    private EmulatorStatus status;      // 애뮬레이터 상태

    @Builder
    public EmulatorEntity(String carNumber, EmulatorStatus status) {
        this.carNumber = carNumber;
        this.status = status;
    }
}
