package com.salary.plus.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RoleType {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_CEO,
    ROLE_INVESTOR,
    ROLE_MANAGER,
    ROLE_DJ,
    ROLE_MAMA,
    ROLE_MODEL,
    ROLE_WAITRESS,
    ROLE_SUPERVISOR;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static RoleType fromValue(String value) {
        try {
            return RoleType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("지원하지 않는 RoleType '%s' 타입입니다.".formatted(value), e);
        }
    }
}
