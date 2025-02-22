package com.salary.plus.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SalesItemType {
    LONG_BAR_FINE,
    SHORT_BAR_FINE,
    EVENT_BAR_FINE,
    LADY_DRINK,
    GUEST_DRINK,
    LOCAL_BEER;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SalesItemType fromValue(String value) {
        try {
            return SalesItemType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("지원하지 않는 SalesItemType '%s' 타입입니다.".formatted(value), e);
        }
    }
}
