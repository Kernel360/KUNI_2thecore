package com.example._thecore_back.rest.admin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminRegisterRequest {
    private String loginId;
    private String password;
    private String name;
    private String phoneNumber;
    private String email;
    private java.time.LocalDateTime birthdate;
}
