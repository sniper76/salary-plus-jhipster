package com.salary.plus.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ShopType {
    BAR,
    RESTAURANT;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ShopType fromValue(String value) {
        try {
            return ShopType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("지원하지 않는 ShopType '%s' 타입입니다.".formatted(value), e);
        }
    }
}
