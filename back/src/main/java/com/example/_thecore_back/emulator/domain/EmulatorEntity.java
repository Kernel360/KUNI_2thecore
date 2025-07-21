package com.example._thecore_back.emulator.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "emulator")
public class EmulatorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false, length = 20)
    private String deviceId; // 디바이스 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EmulatorStatus status; // 애뮬레이터 상태 (ON/OFF)

    @Transient
    private String carNumber;

    @Builder
    public EmulatorEntity(String deviceId, EmulatorStatus status) {
        this.deviceId = deviceId;
        this.status = status;
    }
}
