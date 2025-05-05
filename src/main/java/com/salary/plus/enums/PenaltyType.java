package com.salary.plus.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;

public enum PenaltyType {
    DAY,
    MANDATORY,
    BAR_SHARE,
    DRESS,
    MAKEUP,
    TIME;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PenaltyType fromValue(String value) {
        try {
            return PenaltyType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("지원하지 않는 PenaltyType '%s' 타입입니다.".formatted(value), e);
        }
    }

    private static List<PenaltyType> DAY_PENALTY = List.of(DAY, MANDATORY);

    private static List<PenaltyType> DAY_WITH_PENALTY = List.of(DAY, MANDATORY, BAR_SHARE, TIME);

    public static List<String> getDayPenalty() {
        return DAY_PENALTY.stream().map(PenaltyType::name).toList();
    }

    public static List<PenaltyType> getDayWithTimePenalty() {
        return DAY_WITH_PENALTY;
    }
}
