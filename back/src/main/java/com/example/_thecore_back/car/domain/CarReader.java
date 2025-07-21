package com.example._thecore_back.car.domain;

import javax.swing.plaf.OptionPaneUI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CarReader  {
    Optional<CarEntity> findByCarNumber(String carNumber);

    List<CarEntity> findAll();

    Map<CarStatus, Long> getCountByStatus();

    Optional<CarEntity> findByEmulatorId(Integer emulatorId);
}
