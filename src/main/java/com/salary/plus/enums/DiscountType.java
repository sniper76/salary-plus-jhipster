package com.salary.plus.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DiscountType {
    PERCENT,
    PRICE;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static DiscountType fromValue(String value) {
        try {
            return DiscountType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("지원하지 않는 DiscountType '%s' 타입입니다.".formatted(value), e);
        }
    }
}
