package com.example.common.domain.car;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CarReader  {
    Optional<CarEntity> findByCarNumber(String carNumber);

    Page<CarEntity> findAll(Pageable pageable);

    Map<CarStatus, Long> getCountByStatus();

    List<CarEntity> findByStatus(List<CarStatus> statuses);

}
