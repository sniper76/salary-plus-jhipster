package com.salary.plus.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BonusType {
    PLUS_SALARY,
    SALARY_BONUS,
    COMMISSION;

    public int getPrice(int basePrice) {
        return basePrice;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static BonusType fromValue(String value) {
        try {
            return BonusType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("지원하지 않는 BonusType '%s' 타입입니다.".formatted(value), e);
        }
    }
}
