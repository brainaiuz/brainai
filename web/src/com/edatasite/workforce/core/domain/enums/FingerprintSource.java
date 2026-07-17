package com.edatasite.workforce.core.domain.enums;

public enum FingerprintSource {
    FACE_ID,
    MOBILE,
    WEB,
    UNKNOWN;

    public static FingerprintSource from(String name) {
        if (name == null) return UNKNOWN;
        return switch (name) {
            case "FACE_ID" -> FACE_ID;
            case "MOBILE" -> MOBILE;
            case "WEB" -> WEB;
            default -> UNKNOWN;
        };
    }
}
