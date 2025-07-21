package com.example._thecore_back.car.domain;

public enum CarStatus {
    IN_USE("운행"),        // 운행 중
    IDLE("대기"),          // 대기 중
    MAINTENANCE("수리");   // 수리 중

    private final String displayName;

    CarStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // 한글 문자열로부터 enum 얻는 메서드
    public static CarStatus fromDisplayName(String displayName) {
        for (CarStatus status : values()) {
            if (status.getDisplayName().equals(displayName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 status: " + displayName);
    }
}
