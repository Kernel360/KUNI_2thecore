package com.example._thecore_back.rest.admin.controller;

import com.example._thecore_back.rest.admin.model.AdminRegisterRequest;
import com.example._thecore_back.rest.admin.model.AdminResponse;
import com.example._thecore_back.rest.admin.model.AdminUpdateRequest;
import com.example._thecore_back.rest.admin.service.AdminService;

import com.example._thecore_back.rest.car.model.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminApiController {

    private final AdminService adminService;

    @PostMapping("/signup")
    public ResponseEntity<Api<AdminResponse>> registerAdmin(@RequestBody AdminRegisterRequest requestDto) {
        AdminResponse responseDto = adminService.registerAdmin(requestDto);
        Api<AdminResponse> response = Api.<AdminResponse>builder()
                .result(String.valueOf(HttpStatus.CREATED.value()))
                .data(responseDto)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{loginId}")
    public ResponseEntity<Api<AdminResponse>> updateAdmin(@PathVariable String loginId, @RequestBody AdminUpdateRequest requestDto) {
        AdminResponse responseDto = adminService.updateAdmin(loginId, requestDto);
        Api<AdminResponse> response = Api.<AdminResponse>builder()
                .result(String.valueOf(HttpStatus.OK.value()))
                .data(responseDto)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{loginId}")
    public ResponseEntity<Api<String>> deleteAdmin(@PathVariable String loginId) {
        String deletedId = adminService.deleteAdmin(loginId);
        Api<String> response = Api.<String>builder()
                .result(String.valueOf(HttpStatus.OK.value()))
                .data(deletedId + " admin deleted successfully.")
                .build();
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Api<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        Api<String> response = Api.<String>builder()
                .result(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Api<String>> handleException(Exception e) {
        Api<String> response = Api.<String>builder()
                .result(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
