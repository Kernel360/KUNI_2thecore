package com.example._thecore_back.common.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean result;

    private String message;

    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, null, data);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> successWithNoData(String message){
        return new ApiResponse<>(true, message, null);
    }

    public static <T> ApiResponse<T> fail(T data){ return new ApiResponse<>(false, null, data); }
}
