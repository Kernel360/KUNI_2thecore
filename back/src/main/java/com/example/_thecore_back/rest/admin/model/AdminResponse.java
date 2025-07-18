package com.example._thecore_back.rest.admin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponse {
    private String loginId;
    private String name;
    private String phoneNumber;
    private String email;
    private LocalDateTime birthdate;
}
