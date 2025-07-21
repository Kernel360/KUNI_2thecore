package com.example._thecore_back.car.exception;

public enum CarErrorCode {

    CAR_NOT_FOUND_BY_NUMBER("해당 차량 ( %s )은 존재하지 않습니다. 다시 입력해주세요"),
    NO_REGISTERED_CAR("등록된 차량이 존재하지 않습니다.");

    private final String message;

    CarErrorCode(String message) {
        this.message = message;
    }

    public String format(Object... args){
        return String.format(this.message, args);
    }

    public String getMessage(){
        return this.message;
    }

}
