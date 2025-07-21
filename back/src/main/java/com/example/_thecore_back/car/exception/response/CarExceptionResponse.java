package com.example._thecore_back.car.exception.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarExceptionResponse {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String message;

    private String path;
}
