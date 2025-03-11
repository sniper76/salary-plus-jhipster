package com.salary.plus.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PenaltyType {
    DAY,
    TIME;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PenaltyType fromValue(String value) {
        try {
            return PenaltyType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("지원하지 않는 PenaltyType '%s' 타입입니다.".formatted(value), e);
        }
    }
}
